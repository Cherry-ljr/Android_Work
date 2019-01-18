package DataBase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {
	private final static int DATABASE_VERSION = 1;// 数据库版本号
	private final static String DATABASE_NAME = "students.db";// 数据库名

	private static Context context;

	public static void setContext(Context context) {
		Database.context = context;
	}

	public Database() {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE students(" 
	            + "Stu     VARCHAR(30)	NOT NULL,"
				+ "Name	    VARCHAR(30)	NOT NULL,"
				+ "Gender	VARCHAR(30)	NOT NULL,"
				+ "Math	    VARCHAR(30)	NOT NULL,"
				+ "English	VARCHAR(30)	NOT NULL,"
				+ "Physics  VARCHAR(30)	NOT NULL) ;";
		db.execSQL(sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public SQLiteDatabase getConnection() {
		SQLiteDatabase db = getWritableDatabase();//以读写方式打开数据库
		return db;
	}

	public void close(SQLiteDatabase db) {
		db.close();
	}

}

