package ch.epfl.calendar.persistence;

/**
 * DAO for {@link Event}
 *
 * @author lweingart
 *
 */
public class EventDataSource implements DAO {

	private static final String ERROR_CREATE 	= "Unable to create a new event!";
	private static final String ERROR_DELETE 	= "Unable to delete a event!";
	private static final String ERROR_UPDATE 	= "Unable to update a event!";

	private static final String SUCCESS_CREATE 	= "Event successfully created!";
	private static final String SUCCESS_DELETE 	= "Event successfully deleted";
	private static final String SUCCESS_UPDATE 	= "Event successfully updated";

	private static EventDataSource mEventDataSource;

	public static EventDataSource getInstance() {
		if (EventDataSource.mEventDataSource == null) {
			EventDataSource.mEventDataSource = new EventDataSource();
		}
		return EventDataSource.mEventDataSource;
	}

	@Override
	public void create(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
