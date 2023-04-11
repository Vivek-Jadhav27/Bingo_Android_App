package com.example.gridview;

import static java.lang.Integer.SIZE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    GridView gridView2;
    private int[][] matrix = new int[5][5];
    private int[][] matrixC = new int[5][5];
    private List<Integer> numbersList = new ArrayList<>();
    private List<Integer> remainnumbersList = new ArrayList<>();
    private List<Integer> EnterednumbersList = new ArrayList<>();
    private Random random = new Random();
    int flage = 0;
    int userturn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        gridView = findViewById(R.id.grid_matrix);
        creatboard();
        Adapter1 adapter1 = new Adapter1();
        gridView.setAdapter(adapter1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnterednumbersList.add((Integer) adapter1.getItem(position));
                remainnumbersList.remove(adapter1.getItem(position));
                int row = position / matrix[0].length;
                int col = position % matrix[0].length;
                matrixC[row][col] = 1;
                flage = position;
                View cur = gridView.getChildAt(position);
                cur.setBackgroundResource(R.drawable.bg22);

                adapter1.notifyDataSetChanged();
                if (checkForWin(matrixC)) {
                    // Show win message
                    Toast.makeText(MainActivity.this, "You won!", Toast.LENGTH_SHORT).show();
                    showWindailouge();
                }
            }
        });

    }

    private void showWindailouge() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
        View view =LayoutInflater.from(MainActivity.this).inflate(R.layout.youwin,(ConstraintLayout)findViewById(R.id.layoutDailouge));
        builder.setView(view);
        TextView title;
        TextView body;
        Button Retry;

        title = view.findViewById(R.id.textTitle);
        body = view.findViewById(R.id.textbody);
        Retry =view.findViewById(R.id.replay);

        title.setText("You Won");
        body.setText("Do you Want to Replay");
        Retry.setText("Retry");

        final AlertDialog alertDialog = builder.create();
        Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });

        if (alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void creatboard() {

        for(int i = 1; i <= 25; i++) {
            numbersList.add(i);
            remainnumbersList.add(i);

        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int number = getRandomNumber();
                matrix[i][j] = number;
                matrixC[i][j] = 0;
            }
        }
    }
    private class Adapter1 extends BaseAdapter {


        private AdapterView.OnItemClickListener listener;
        private boolean isLayout1 = true;
        private Map<Integer, Boolean> layoutMap;


        @Override
        public int getCount() {
            return 25;
        }

        @Override
        public Object getItem(int position) {
            return matrix[position / 5][position % 5];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView;


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (shouldUseLayout1(position)) {
                    convertView = inflater.inflate(R.layout.item_matrix, parent, false);

                } else {
                    convertView = inflater.inflate(R.layout.item_matrix2, parent, false);

                }
            }
            textView = convertView.findViewById(R.id.text_matrix_item);
            textView.setText(String.valueOf(matrix[position / 5][position % 5]));

            return convertView;
        }

        private boolean shouldUseLayout1(int position) {


            if(matrixC[position/5][position%5] == 1 ){
                return false;
            }
            else
            {
                return true;
            }
        }
    }
    private int getRandomNumber() {
        int index = random.nextInt(numbersList.size());
        int number = numbersList.get(index);
        numbersList.remove(index);
        return number;
    }

    private boolean checkForWin(int[][] matrix) {

        int win =0;
        // Check for win in rows
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row][0] == 1 &&
                    matrix[row][1] == 1 &&
                    matrix[row][2] == 1 &&
                    matrix[row][3] == 1 &&
                    matrix[row][4] == 1) {
                win++;
            }
        }

        // Check for win in columns
        for (int col = 0; col < matrix[0].length; col++) {
            if (matrix[0][col] == 1 &&
                    matrix[1][col] == 1 &&
                    matrix[2][col] == 1 &&
                    matrix[3][col] == 1 &&
                    matrix[4][col] == 1) {
                win++;
            }
        }

        // Check for win in first diagonal
        if (matrix[0][0] == 1 &&
                matrix[1][1] == 1 &&
                matrix[2][2] == 1 &&
                matrix[3][3] == 1 &&
                matrix[4][4] == 1) {
            win++;
        }

        // Check for win in second diagonal
        if (matrix[0][4] == 1 &&
                matrix[1][3] == 1 &&
                matrix[2][2] == 1 &&
                matrix[3][1] == 1 &&
                matrix[4][0] == 1) {
            win++;
        }

        // No win found
        if (win == 5){
            return true;
        }

        return false;
    }
    @Override
    public void onBackPressed(){


        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Exit Game");
        alert.setMessage("Do you want to exit Game");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }
}