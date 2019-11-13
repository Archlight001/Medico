package denokela.com.medico;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface UserDao {
    @Insert
    void insertAll(User... user);

    @Query("SELECT * FROM user")
    List<User> getAllResults();
}
