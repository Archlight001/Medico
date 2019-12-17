package denokela.com.medico.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import denokela.com.medico.entities.UserEntity;
import denokela.com.medico.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<UserEntity>> allUsers;
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }

    public void insert(UserEntity userEntity){
        repository.insert(userEntity);
    }
    public void update(UserEntity userEntity){
        repository.update(userEntity);
    }
    public void delete(UserEntity userEntity){
        repository.delete(userEntity);
    }

    public void deleteAllUsers(){
        repository.deleteAllUsers();
    }
    public LiveData<List<UserEntity>> getAllUsers(){
        return  allUsers;
    }
}
