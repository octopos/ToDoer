package com.wtm.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.wtm.database.ToDoItem;
import com.wtm.database.ToDoItemDataSource;
import com.wtm.database.UserDataSource;
import com.wtm.database.Users;

@SuppressWarnings("serial")
public class SyncServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BufferedReader requestReader = null;
		PrintWriter responseWriter = resp.getWriter();
		// System.out.println("Got writer "+responseWriter);
		try {
			InputStream inputStream = req.getInputStream();
			System.out.println("Got stream " + req.getContentLength());
			if (inputStream != null) {
				requestReader = new BufferedReader(new InputStreamReader(
						inputStream));
				String line;
				String[] columns;
				line = requestReader.readLine();// User line
				columns = line.split("\t");
				if (columns[0].equals("Get Users")) {
					UserDataSource userDS = new UserDataSource();
					ArrayList<Users> existingUsers = (ArrayList<Users>)userDS.getAllUsers();
					String sendString = "";
					for (Users oneUser : existingUsers) {
						sendString+=oneUser.toSyncString();
					}
					responseWriter.write(sendString);

				} else {
					Users targetUser = synchronizeUser(columns[0], columns[1]);
					long targetUserId = UserDataSource.getUserID(targetUser.getName());
					ArrayList<TaskChange> mobileChangesList = new ArrayList<TaskChange>();
					ArrayList<TaskChange> mobileDeleteList = new ArrayList<TaskChange>();
					while ((line = requestReader.readLine()) != null) {// Changes
																		// Lines
						System.out.println(line);
						columns = line.split("\t");
						if (columns[1].equals(ActionType.Delete.toString())) {
							TaskChange newChange = new TaskChange(columns[0],
									columns[1], columns[2]);
							mobileDeleteList.add(newChange);
							System.out.println("Created new delete item : "
									+ newChange.toSyncString());
						} else {
							TaskChange newChange = new TaskChange(columns[0],
								columns[1], columns[2], targetUserId, columns[3],
									columns[4], columns[5], columns[6],
									columns[7], columns[8]);
							mobileChangesList.add(newChange);
							System.out.println("Created new change item : "
									+ newChange.toSyncString()
									+ newChange.item.toSyncString());
						}
					}

					ArrayList<ToDoItem> resultItems = synchronizeChanges(
							targetUser.getName(), mobileChangesList);
					ArrayList<TaskChange> resultDelList = synchronizeDeletes(mobileDeleteList);
					responseWriter.write(targetUser.getName() + "\t"
							+ targetUser.getEncryptedPwd() + "\n");
					for (TaskChange change : resultDelList) {
						// responseWriter.write("<br/>");// Comment out for
						// android
						responseWriter.write(ActionType.Delete + "\t"
								+ change.getTaskID() + "\n");
					}
					for (ToDoItem item : resultItems) {
						// responseWriter.write("<br/>"); // Comment out for
						// android
						responseWriter.write(ActionType.Add + "\t"
								+ item.toSyncString());
					}
					
					ToDoItemDataSource itemDS = new ToDoItemDataSource();
					ChangeTracker tracker = new ChangeTracker(itemDS);
					tracker.deleteChangesByUsername(targetUser.getName());
					
				}
			} else {
				responseWriter.write("No Changes");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			// throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (requestReader != null) {
				try {
					requestReader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
					// throw ex;
				}
			}
		}
	}

	private Users synchronizeUser(String username, String password) {
		UserDataSource userDS = new UserDataSource();
		Users targetUser = userDS.selectUser(username);
		if (targetUser != null)
			return targetUser;
		else {
			targetUser = new Users();
			targetUser.setName(username);
			targetUser.setEncryptedPwd(password);
			userDS.createUsers(targetUser.getName(), targetUser.getPwd());
			return targetUser;
		}
	}

	private ArrayList<TaskChange> synchronizeDeletes(
			ArrayList<TaskChange> mobileDeleteList) {
		ToDoItemDataSource itemDS = new ToDoItemDataSource();

		for (TaskChange change : mobileDeleteList) {
			System.out.println("Deleting : " + change);
			itemDS.deleteItemById(change.getTaskID());
		}

		ChangeTracker tracker = new ChangeTracker(itemDS);
		ArrayList<TaskChange> webDeleteList = tracker.getTaskDeletes();
		webDeleteList.removeAll(mobileDeleteList);
		return webDeleteList;

	}

	private ArrayList<ToDoItem> synchronizeChanges(String username,
			ArrayList<TaskChange> mobileChangesList) {
		System.out.println("In sync changes : ");
		ToDoItemDataSource parentDS = new ToDoItemDataSource();
		ChangeTracker tracker = new ChangeTracker(parentDS);
		ArrayList<TaskChange> webChangesList = tracker
				.getTaskChangesByUsername(username);
		ArrayList<TaskChange> webAddList = new ArrayList<TaskChange>();
		ArrayList<TaskChange> mobAddList = new ArrayList<TaskChange>();
		ArrayList<TaskChange> webUpList = new ArrayList<TaskChange>();
		ArrayList<TaskChange> mobUpList = new ArrayList<TaskChange>();
		ArrayList<ToDoItem> sendAddList = new ArrayList<ToDoItem>();

		String add = ActionType.Add.toString();
		String edit = ActionType.Update.toString();

		for (TaskChange wchange : webChangesList) {
			if (add.equals(wchange.getAction().toString()))
				webAddList.add(wchange);
			else if (edit.equals(wchange.getAction().toString()))
				webUpList.add(wchange);
		}

		for (TaskChange mchange : mobileChangesList) {
			if (add.equals(mchange.getAction().toString()))
				mobAddList.add(mchange);
			else if (edit.equals(mchange.getAction().toString()))
				mobUpList.add(mchange);
		}

		sendAddList.addAll(synchronizeAdds(username, webAddList, mobAddList));
		sendAddList.addAll(synchronizeUpdates(username, webUpList, mobUpList));

		/*
		 * System.out.println("Adding mobile " + mobileChangesList.size() +
		 * "changes : "); for (TaskChange change : mobileChangesList) {
		 * System.out.println(change); sendAddList.add(change.getItem()); }
		 */
		return sendAddList;
	}

	private ArrayList<ToDoItem> synchronizeAdds( String username,
			ArrayList<TaskChange> webAddList, ArrayList<TaskChange> mobAddList) {
		ArrayList<ToDoItem> sendAddList = new ArrayList<ToDoItem>();
		ToDoItemDataSource itemDS = new ToDoItemDataSource();
		System.out.println("Adding web " + webAddList.size() + "changes : ");
		for (TaskChange wchange : webAddList) {
			System.out.println("Adding : " + wchange);
			sendAddList.add(itemDS.getItemByItemId(wchange.getTaskID()));
		}

		System.out.println("Adding mobile " + mobAddList.size() + "changes : ");
		for (TaskChange mchange : mobAddList) {
			System.out.println("Adding : " + mchange);
			sendAddList.add(itemDS.createItem(username, mchange.getItem()));
		}
		return sendAddList;
	}

	private ArrayList<ToDoItem> synchronizeUpdates(String username,
			ArrayList<TaskChange> webUpList, ArrayList<TaskChange> mobUpList) {
		ArrayList<ToDoItem> sendAddList = new ArrayList<ToDoItem>();
		
		ArrayList<TaskChange> webConfList = new ArrayList<TaskChange>();
		ArrayList<TaskChange> mobConfList = new ArrayList<TaskChange>();
		ArrayList<TaskChange> webUniqList = new ArrayList<TaskChange>();
		ArrayList<TaskChange> mobUniqList = new ArrayList<TaskChange>();
		
		boolean unique;
		System.out.println("Checking " + webUpList.size() + " web changes : ");
		for (TaskChange wchange : webUpList) {
			unique=true;
			for (TaskChange mchange : mobUpList) {
				if(wchange.taskID==mchange.taskID)
				{
					unique=false;
					break;
				}
			}
			if(unique)
				webUniqList.add(wchange);
			else
				webConfList.add(wchange);
		}

		System.out.println("Checking " + mobUpList.size() + " mobile changes : ");
		for (TaskChange mchange : mobUpList) {
			unique=true;
			for (TaskChange wchange : webUpList) {
				if(mchange.taskID==wchange.taskID)
				{
					unique=false;
					break;
				}
			}
			if(unique)
				mobUniqList.add(mchange);
			else
				mobConfList.add(mchange);
		}
		
		sendAddList.addAll(synchronizeUniqUpdates(username, webUniqList, mobUniqList));
		sendAddList.addAll(resolveConflicts(username,webConfList,mobConfList));
		
		
		return sendAddList;
	}

	private ArrayList< ToDoItem> synchronizeUniqUpdates(String username,
			ArrayList<TaskChange> webUniqList, ArrayList<TaskChange> mobUniqList) {
		ArrayList<ToDoItem> sendAddList = new ArrayList<ToDoItem>();
		ToDoItemDataSource itemDS = new ToDoItemDataSource();
		System.out.println("Updating web " + webUniqList.size() + "changes : ");
		for (TaskChange wchange : webUniqList) {
			System.out.println("Adding update : " + wchange);
			sendAddList.add(itemDS.getItemByItemId(wchange.getTaskID()));
		}

		System.out.println("Updating mobile " + mobUniqList.size() + "changes : ");
		for (TaskChange mchange : mobUniqList) {
			System.out.println("Adding update: " + mchange);
			itemDS.updateItem(mchange.getItem(),false);
			sendAddList.add(mchange.getItem());
		}
		return sendAddList;
	
	}
	
		
	private ArrayList< ToDoItem> resolveConflicts(String username,
			ArrayList<TaskChange> webConfList, ArrayList<TaskChange> mobConfList) {
		
		ArrayList<ToDoItem> sendAddList = new ArrayList<ToDoItem>();
		ToDoItemDataSource itemDS = new ToDoItemDataSource();
		
		//Web changes overwrite mobile ones.
		System.out.println("Resolving conflicts of " + webConfList.size() + "changes");
		for (TaskChange wchange : webConfList) {
			/*
			final long targetId = wchange.getTaskID();
			Collections2.filter(mobConfList, new Predicate<TaskChange>() {
			    public boolean apply(TaskChange arg) { return arg.getTaskID() == targetId; }
			});
			if(wchange.getTimestamp().compareTo(anotherDate))
			*/
			sendAddList.add(itemDS.getItemByItemId(wchange.getTaskID()));
		}

		/*
		System.out.println("Adding mobile " + mobAddList.size() + "changes : ");
		for (TaskChange mchange : mobAddList) {
			System.out.println("Adding : " + mchange);
			sendAddList.add(itemDS.createItem(username, mchange.getItem()));
		}
		*/
		return sendAddList;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);

	}

}
