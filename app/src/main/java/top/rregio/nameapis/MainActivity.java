package top.rregio.nameapis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView txtNome;
    Button btnVerificaNomes;
    String nomes;
    TextView txtResult;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNome= findViewById(R.id.txtNomes);
        btnVerificaNomes = findViewById(R.id.btnViewInfo);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        txtResult = findViewById(R.id.txtResult);
        txtResult.setText("Resultado:\n");
    }
    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if((networkInfo != null) && (networkInfo.isConnectedOrConnecting())) {
            return true;
        }else{
            return false;
        }
    }
    public void verificaNomes(View view){
        nomes = txtNome.getText().toString();
        String[] nomesList = nomes.split(",");

        StringBuilder stringBuilder;
        if(nomesList.length>1) {
            stringBuilder = new StringBuilder("https://api.genderize.io/?&");
            for (String nome : nomesList) {
                stringBuilder.append("&name="+nome);
            }
        }else{
            stringBuilder = new StringBuilder("https://api.genderize.io?name="+nomesList[0]);
        }

        if(isOnline()){
            buscaDados(stringBuilder.toString());
        }else{
            Toast.makeText(this,"Rede não disponível!", Toast.LENGTH_LONG).show();
        }
    }

    protected void atualizaView(String mensagem) {
        txtResult.setText("Resultado: "+mensagem);
    }

    private void buscaDados(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    private class MyTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPostExecute(String s) {
            atualizaView(s);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String conteudo = HttpManager.getDados(params[0]);
            return conteudo;
        }
    }
}