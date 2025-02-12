package cc.gilmore.audioplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import cc.gilmore.audioplayer.database.AppDatabase;
import cc.gilmore.audioplayer.model.Song;

public class FileUploadActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private TextView selectedFileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        selectedFileTextView = findViewById(R.id.selected_file_text_view);
        Button selectFileButton = findViewById(R.id.select_file_button);

        selectFileButton.setOnClickListener(v -> performFileSearch());
    }

    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                String fileName = getFileName(uri);
                selectedFileTextView.setText(fileName);
                uploadFile(uri, fileName);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadFile(Uri uri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getFilesDir(), fileName);
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // 4k buffer
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
            saveSongToDatabase(fileName, file.getAbsolutePath());
            Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("FileUploadActivity", "Error uploading file", e);
            Toast.makeText(this, "Error uploading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSongToDatabase(String fileName, String filePath) {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        Song song = new Song(fileName, "Unknown Artist", "Unknown Album", filePath, null, 0);
        new Thread(() -> db.songDao().insert(song)).start();
    }
}

