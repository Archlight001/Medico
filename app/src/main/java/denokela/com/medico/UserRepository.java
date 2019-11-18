package denokela.com.medico;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<UserEntity>> allUsers;

    public UserRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        userDao = appDatabase.userDao();
        allUsers = userDao.getAllUsers();
    }

    public void insert(UserEntity userEntity){
        new AsyncTasker(userDao,"insert").execute(userEntity);
    }

    public void update(UserEntity userEntity){
        new AsyncTasker(userDao,"update").execute(userEntity);
    }

    public void delete(UserEntity userEntity){
        new AsyncTasker(userDao,"delete").execute(userEntity);
    }

    public void deleteAllUsers(){
        new AsyncTasker(userDao,"deleteAll").execute();
    }

    public LiveData<List<UserEntity>> getAllUsers(){

        return allUsers;
    }


    private static class AsyncTasker extends android.os.AsyncTask<UserEntity,Void,Void>{
        private UserDao userDao;
        private String action;

        private AsyncTasker(UserDao userDao, String action){
            this.userDao = userDao;
            this.action = action;
        }


        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            if(action.equals("insert")){
                userDao.insert(userEntities[0]);
            }else if(action.equals("update")){
                userDao.update(userEntities[0]);
            }else if(action.equals("delete")){
                userDao.delete(userEntities[0]);
            }else if(action.equals("deleteAll")){
                userDao.deleteAllUser();
            }
            return null;
        }
    }
}
