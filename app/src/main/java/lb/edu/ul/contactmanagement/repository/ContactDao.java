package lb.edu.ul.contactmanagement.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact where fname Like :fname order By fname Asc")
    LiveData<List<Contact>> getAllContacts(String fname);

    @Query("SELECT * FROM Contact where uid= :id")
    LiveData<Contact> getContactById(int id);

    @Insert
    void insertAll(Contact... contacts);

    @Update
    void updateContact(Contact contact);

    @Delete
    void delete(Contact contact);
}