package denokela.com.medico.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import denokela.com.medico.entities.UserEntity;

@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

    @Query("DELETE FROM user_table WHERE Userid=:value")
    void delete(Integer value);

    @Query("DELETE FROM user_table")
    void deleteAllUser();

    @Query("SELECT * FROM user_table")
    LiveData<List<UserEntity>> getAllUsers();
}
