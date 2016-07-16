package com.taxiapp.db;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.taxiapp.model.business.Booking;

import java.sql.SQLException;

@SuppressWarnings("unchecked")
public class BookingDao extends BaseDaoImpl<Booking, String> {

    public static String[] minimumColumnSet = {"bookingId"};

    public BookingDao(ConnectionSource connectionSource, Class<Booking> dataClass)
            throws SQLException {
        super(connectionSource, dataClass);
    }

    //Sample Queries...
//    public CloseableWrappedIterable<Booking> loadCapturedByAppNo(String appNo) throws Exception {
//        QueryBuilder<Booking, String> builder = queryBuilder();
//        Where<Booking, String> where = builder.where();
//        where.and(
//                where.eq("newAppNo", appNo),
//                where.in(
//                        "lifecycleStatus",
//                        Arrays.asList(new EnrollmentStatus[]{EnrollmentStatus.E_CAPTURED})),
//                where.in("syncStatus",
//                        Arrays.asList(new SyncStatus[]{SyncStatus.READY_FOR_SYNC, SyncStatus.SYNC_FAILED})));
//        PreparedQuery<Booking> prepQuery = builder.prepare();
//
//        return getWrappedIterable(prepQuery);
//    }
//


//    public List<Booking> loadAllSubmittedEnr() throws Exception {
//
//        QueryBuilder<Booking, String> builder = queryBuilder();
//        Where<Booking, String> where = builder.where();
//        where.and(where.in(
//                "lifecycleStatus",
//                Arrays.asList(new EnrollmentStatus[]{EnrollmentStatus.E_CAPTURED})), where.in("syncStatus",
//                Arrays.asList(new SyncStatus[]{SyncStatus.READY_FOR_SYNC, SyncStatus.SYNC_FAILED})));
//
//        List<Booking> list = where.query();
//        if (list == null) {
//            list = new ArrayList<>();
//        }
//        return list;
//    }
//
//    public long countOfSyncSuccess() {
//        try {
//            QueryBuilder<Booking, String> builder = queryBuilder();
//            List<SyncStatus> statuses = new ArrayList<SyncStatus>();
//            statuses.add(SyncStatus.SYNC_SUCCESS);
//            builder.setCountOf(true);
//            long successCount = builder.where().in("syncStatus", statuses).countOf();
//            return successCount;
//        } catch (Exception e) {
//        }
//        return 0;
//    }
//

//
//    public Booking getLast(String batchId) throws Exception {
//        QueryBuilder<Booking, String> queryBuilder = queryBuilder();
//        queryBuilder().where().eq("newAppNo", batchId);
//        queryBuilder.selectColumns("branchCode", "newAppNo");
//        List<Booking> list = queryBuilder.query();
//        if (list == null || list.isEmpty()) {
//            return null;
//        }
//        return list.get(0);
//    }
//
//    public long countOfDraftEnr(String brCode, long duration) throws Exception {
//        //1. Query for EnrolIDs
//        QueryBuilder<Booking, String> queryBuilder = queryBuilder();
//        Where<Booking, String> where = queryBuilder.where();
//        where.and(
//                where.eq("branchCode", brCode),
//                where.in("lifecycleStatus", Arrays.asList(new EnrollmentStatus[]{EnrollmentStatus.E_READY, EnrollmentStatus.E_CAPTURED})),
//                where.in("syncStatus", Arrays.asList(new SyncStatus[]{SyncStatus.SYNC_SUCCESS, SyncStatus.INTERMEDIATE_STATE, SyncStatus.SHOULD_NOT_SYNC})),
//                where.lt("capturedDateTime", duration));
//        queryBuilder.selectColumns("enrollmentId", "newAppNo");
//        queryBuilder.setCountOf(true);
//        AppLogger.get().i(getClass(), queryBuilder.prepare().toString());
//        return queryBuilder.countOf();
//    }
//
//    public int skipCustomer(Booking enrolDetails, String agentId) throws Exception {
//        UpdateBuilder<Booking, String> builder = updateBuilder();
//        builder.where().eq("enrollmentId", enrolDetails.getEnrollmentId());
//        builder.updateColumnValue("lifecycleStatus", EnrollmentStatus.E_CAPTURED);
//        builder.updateColumnValue("fvrStatus", FVRStatus.FVR_COMPLETED);
//        builder.updateColumnValue("formStatus", FormStatus.DROPPED.name());
//        builder.updateColumnValue("remarks", enrolDetails.getRemarks());
//        builder.updateColumnValue("agentCapturedId", agentId);
//        builder.updateColumnValue("syncStatus", SyncStatus.READY_FOR_SYNC);
//        builder.updateColumnValue("capturedDateTime", SystemTime.currentTimeMillis());
//
//        enrolDetails.setLifecycleStatus(EnrollmentStatus.E_CAPTURED);
//        enrolDetails.setFormStatus(FormStatus.DROPPED.name());
//
//        builder.updateColumnValue("enrollmentType", EnrolmentType.NEW);
//
//        int update = update(builder.prepare());
//
//        // TransactionDBManager.getInstance().getEnrolProofDao().deleteById(enrolDetails.getEnrollmentId());
//
//
//        return update;
//
//    }
//
}
