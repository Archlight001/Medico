package denokela.com.medico.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class UserEntity {
    //Entity Class for the UserEntity Database

    public UserEntity(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }


    @PrimaryKey(autoGenerate = true)
    private int Userid;

    @ColumnInfo(name = "FirstName")
    String firstName;

    @ColumnInfo(name = "LastName")
    String lastName;


    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
