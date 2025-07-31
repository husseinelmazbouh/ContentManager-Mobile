package lb.edu.ul.contactmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lb.edu.ul.contactmanagement.repository.Contact;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    List<Contact> contacts;
    Context context;

    public CustomAdapter(Context c, List<Contact> conts) {
        context = c;
        contacts = conts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView phoneTextView;
        private final ImageButton editButton, deleteButton, callButton;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTv);
            phoneTextView = view.findViewById(R.id.phoneTv);
            editButton =  view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            callButton = view.findViewById(R.id.callButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        MyAlertDialog dialog = new MyAlertDialog(contacts.get(position));
                        dialog.show(((MainActivity)context).getSupportFragmentManager(), "Dialog");
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Intent i = new Intent(context, AddEditContactActivity.class);
                        i.putExtra("contact_id", contacts.get(position).uid);
                        context.startActivity(i);
                    }
                }
            });

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ((MainActivity) context).currentPhone = contacts.get(position).phone;
                    if(position != RecyclerView.NO_POSITION) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                                PackageManager.PERMISSION_GRANTED) {
                            ((MainActivity) context).doTheCall();
                        } else {
                            ((MainActivity) context).launcher.launch(Manifest.permission.CALL_PHONE);
                        }
                    }
                }
            });
        }
        public TextView getNameTextView() {
            return nameTextView;
        }
        public TextView getPhoneTextView() {
            return phoneTextView;
        }
        public ImageButton getEditButton() {
            return editButton;
        }
        public ImageButton getDeleteButton() {
            return deleteButton;
        }
        public ImageButton getCallButton() {
            return callButton;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getNameTextView().setText(contacts.get(position).fname + " " + contacts.get(position).lname);
        holder.getPhoneTextView().setText(contacts.get(position).phone);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
