package denokela.com.medico.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.AppDatabase;
import denokela.com.medico.dao.UserDao;
import denokela.com.medico.entities.UserEntity;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<UserEntity>> allUsers;

    public UserRepository(Application application){
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        userDao = appDatabase.userDao();
        allUsers = userDao.getAllUsers();
    }

    public void insert(UserEntity userEntity){
        new AsyncTasker(userDao,"insert",0).execute(userEntity);
    }

    public void update(UserEntity userEntity){
        new AsyncTasker(userDao,"update",0).execute(userEntity);
    }

    public void delete(Integer value){
        new AsyncTasker(userDao,"delete",value).execute();
    }

    public void deleteAllUsers(){
        new AsyncTasker(userDao,"deleteAll",0).execute();
    }

    public LiveData<List<UserEntity>> getAllUsers(){

        return allUsers;
    }


    private static class AsyncTasker extends android.os.AsyncTask<UserEntity,Void,Void>{
        private UserDao userDao;
        private String action;
        Integer value;

        private AsyncTasker(UserDao userDao, String action,Integer value){
            this.userDao = userDao;
            this.action = action;
            this.value = value;
        }


        @Override
        protected Void doInBackground(UserEntity... userEntities) {
            if(action.equals("insert")){
                userDao.insert(userEntities[0]);
            }else if(action.equals("update")){
                userDao.update(userEntities[0]);
            }else if(action.equals("delete")){
                userDao.delete(value);
            }else if(action.equals("deleteAll")){
                userDao.deleteAllUser();
            }
            return null;
        }
    }
}
