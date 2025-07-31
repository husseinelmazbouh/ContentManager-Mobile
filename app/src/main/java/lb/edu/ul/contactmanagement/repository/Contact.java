package lb.edu.ul.contactmanagement.repository;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "fname")
    public String fname;
    @ColumnInfo(name = "lname")
    public String lname;
    @ColumnInfo(name = "phone")
    public String phone;
}
