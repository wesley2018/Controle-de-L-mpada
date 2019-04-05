package com.example.controle_rele_lampada;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Controle_rele_lampada extends ActionBarActivity implements OnClickListener {
	ImageButton btLampada,btConectar;
	boolean status = true;
	TextView tvStatusLampada;
	EditText et_Ip;
	String L, hostIp = null;
	Handler mHandler;
	long lastPress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		telaIp(); // FAZ A CHAMADA DO MÉTODO "telaIp"
	}
	// MÉTODO "telaIp"
		public void telaIp(){
			setContentView(R.layout.tela_ip); // INICIALIZA A TELA
			et_Ip = (EditText)findViewById(R.id.et_Ip); // ESTANCIA O EDITTEXT
			
	    	btConectar = (ImageButton) findViewById(R.id.btConectar); // ESTANCIA O IMAGEBUTTON
	        btConectar.setOnClickListener(this); // ATIVA O CLICK DO BOTÃO
	    	
	    	if(btConectar.isPressed()){ // SE O BOTÃO FOR PRESSIONADO
	    		onClick(btConectar); // EXECUTA A FUNÇÃO REFERENTE AO BOTÃO
	    	}
	    }
		// MÉTODO "telaPrincipal"
		public void telaPrincipal(){   	
			setContentView(R.layout.activity_controle_rele_lampada); // INICIALIZA A TELA
			
			mHandler = new Handler(); // VARIÁVEL "mHandler" INICIALIZADA
	        mHandler.post(mUpdate);	 // VARIÁVEL "mHandler" CHAMA O MÉTODO "mUpdate"
			
			btLampada = (ImageButton) findViewById(R.id.btLampada);
	    	
	    	btLampada.setOnClickListener(this);
	    	
	    	tvStatusLampada = (TextView) findViewById(R.id.tvStatusLampada);
		
			if(btLampada.isPressed()){ // SE O BOTÃO FOR PRESSIONADO
				onClick(btLampada); // EXECUTA A FUNÇÃO REFERENTE AO BOTÃO
			}			
		}
		// MÉTODO QUE EXECUTA A ATUALIZAÇÃO DO TEXTVIEW COM INFORMAÇÃO RECEBIDAS DO ARDUINO
		private Runnable mUpdate = new Runnable() {
	    	public void run() {
	    		arduinoStatus("http://"+hostIp+"/"); // CHAMA O MÉTODO "arduinoStatus"
	    		mHandler.postDelayed(this, 500); // TEMPO DE INTERVALO PARA ATUALIZAR NOVAMENTE A INFORMAÇÃO (500 MILISEGUNDOS)
	    	}
	    };
	    public void arduinoStatus(String urlArduino){
			
			String urlHost = urlArduino;
			String respostaRetornada = null;
			
			try{
				respostaRetornada = ConectHttpClient.executaHttpGet(urlHost);
				String resposta = respostaRetornada.toString();
				resposta = resposta.replaceAll("\\s+", "");
				
				String[] b = resposta.split(",");  	     

				if(b[0].equals("AC1")){				
					tvStatusLampada.setText("LIGADA");
				}
				else{
					if(b[0].equals("AP1")){
						tvStatusLampada.setText("DESLIGADA");					
					}
				}
			}
			catch(Exception erro){
			}
		}
		@Override
		public void onClick(View bt) { // MÉTODO QUE GERENCIA OS CLICK'S NOS BOTÕES
			
			if(bt == btConectar){ // SE BOTÃO CLICKADO
				if(et_Ip.getText().toString().equals("")){ // SE EDITTEXT ESTIVER VAZIO
					Toast.makeText(getApplicationContext(), // FUNÇÃO TOAST
					"Digite o IP do Ethernet Shield!", Toast.LENGTH_SHORT).show(); // EXIBE A MENSAGEM
				}else{ // SENÃO
				hostIp = et_Ip.getText().toString(); // STRING "hostIp" RECEBE OS DADOS DO EDITTEXT CONVERTIDOS EM STRING
				// FUNÇÃO QUE OCULTA O TECLADO APÓS CLICAR EM CONECTAR
				InputMethodManager escondeTeclado = (InputMethodManager)getSystemService(
			    Context.INPUT_METHOD_SERVICE);
			    escondeTeclado.hideSoftInputFromWindow(et_Ip.getWindowToken(), 0);
				telaPrincipal(); // FAZ A CHAMADA DO MÉTODO "telaPrincipal"
				}	
			}
			
	String url = null; // CRIA UMA STRING CHAMADA "url" QUE POSSUI VALOR NULO
			
			if(bt == btLampada){ // SE BOTÃO CLICKADO
				url = "http://"+hostIp+"/?L=1"; // STRING "url" RECEBE O VALOR APÓS O SINAL DE "="
			}
			
			String urlGetHost = url; // CRIA UMA STRING CHAMADA "urlGetHost" QUE RECEBE O VALOR DA STRING "url"
			
			//INICIO DO TRY CATCH
			try{
				ConectHttpClient.executaHttpGet(urlGetHost); // PASSA O PARÂMETRO PARA O O MÉTODO "executaHttpGet" NA CLASSE "ConectHttpClient" E ENVIA AO ARDUINO
			}
			catch(Exception erro){ // FUNÇÃO DE EXIBIÇÃO DO ERRO
			} // FIM DO TRY CATCH	
		}
		// MÉTODO QUE VERIFICA O BOTÃO DE VOLTAR DO DISPOSITIVO ANDROID E ENCERRA A APLICAÇÃO SE PRESSIONADO 2 VEZES SEGUIDAS
	    public void onBackPressed() {		
		    long currentTime = System.currentTimeMillis();
		    if(currentTime - lastPress > 5000){
		        Toast.makeText(getBaseContext(), "Pressione novamente para sair.", Toast.LENGTH_LONG).show();
		        lastPress = currentTime;  
		    }else{
		        super.onBackPressed();
		        android.os.Process.killProcess(android.os.Process.myPid());
		    }
		}
}