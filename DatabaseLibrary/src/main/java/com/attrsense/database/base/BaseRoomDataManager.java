//package com.attrsense.database.base;
//
//import android.app.Application;
//
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.migration.Migration;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.Locale;
//
///**
// * room数据库管理类
// * 所有子类不可以直接访问只能通过RoomRepository依赖注入的方式获取以保障全局单例
// */
//public abstract class BaseRoomDataManager<T extends RoomDatabase> {
//
//    public T mDatabase;
//    private Class<T> modelClass;
//
//    /**
//     * 获取db
//     *
//     * @param application application
//     * @return RoomDatabase
//     */
//    public T getDB(Application application) {
//        if (mDatabase == null && modelClass != null) {
//            RoomDatabase.Builder<T> roomBuilder = Room.databaseBuilder(application,
//                    modelClass,
//                    db_name());
//            Migration[] migrations = addMigrations();
//            if (migrations != null && migrations.length > 0) {
//                roomBuilder.addMigrations(migrations);
//            }
//            mDatabase = configBuild(roomBuilder).build();
//
//        }
//        return mDatabase;
//    }
//
//    public BaseRoomDataManager() {
//        Type type = getClass().getGenericSuperclass();
//        if (type instanceof ParameterizedType) {
//            modelClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
//        }
//    }
//
//    /**
//     * 自定义ROOM配置
//     *
//     * @param roomBuilder roomBuilder
//     * @return roomBuilder
//     */
//    protected RoomDatabase.Builder<T> configBuild(RoomDatabase.Builder<T> roomBuilder) {
//        return roomBuilder
//                // 默认不允许在主线程中连接数据库
//                // .allowMainThreadQueries()
//                //测试阶段使用，用于每次重启创建新表
////                .fallbackToDestructiveMigration()
//                ;
//    }
//
//    /**
//     * 数据库升级
//     *
//     * @return Migration[]
//     */
//    protected abstract Migration[] addMigrations();
//
//    /**
//     * 设置数据库名字
//     *
//     * @return 名字
//     */
//    protected abstract String db_name();
//
//    /**
//     * 数据库升级语句
//     *
//     * @param database     db
//     * @param tableName    表名
//     * @param columnName   字段名
//     * @param columnType   字段类型
//     * @param defaultValue 默认值
//     */
//    protected static void addColumn(SupportSQLiteDatabase database,
//                                    String tableName,
//                                    String columnName,
//                                    String columnType,
//                                    String defaultValue) {
//        String sqlStr = "ALTER TABLE `%s` ADD `%s` %s default '%s'";
//        String sqlReal = String.format(Locale.ENGLISH,
//                sqlStr,
//                tableName,
//                columnName,
//                columnType,
//                defaultValue);
//        try {
//            database.execSQL(sqlReal);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
