package denokela.com.medico;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface UserDao {
    @Insert
    void insert(UserEntity userEntity);

    @Update
    void update(UserEntity userEntity);

    @Delete
    void delete(UserEntity userEntity);

    @Query("DELETE FROM user_table")
    void deleteAllUser();

    @Query("SELECT * FROM user_table")
    LiveData<List<UserEntity>> getAllUsers();
}
