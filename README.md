# become_ANDROID_SDK_CEDULA_SCAN
SDK para captura del código de barras en la parte posterior de la cédula de ciudadanía colombiana y extracción de información de la base de datos de la Registraduría Nacional de Colombia

 ### Configuraciones de Gradle

**1.** Dentro del archivo **_build.gradle_** debe adicionar el siguiente fragmento de código:

    android {
      compileOptions {
        sourceCompatibility = 1.8
        targetCompatibility = 1.8
      }
    }
    
**2.** El archivo **_build.gradle_** debe contar con una referencia al repositorio:

	maven { url 'https://jitpack.io'}
<p align="center">
  <img src="https://github.com/Becomedigital/become_ANDROID_SDK_BC/blob/main/build_gradle.png">
</p>

### Implementación de módulos requeridos

Es necesaria la implementacion de los siguientes módulos:

	implementation 'com.google.android.gms:play-services-vision:20.1.3'

### Implementación de la SDK Become

**1.**  Abra el archivo **_build.gradle_** dentro del directorio del módulo de su aplicación e incluya las siguientes dependencias:
    
    implementation 'com.github.Becomedigital:become_ANDROID_SDK_BC:LATEST_VERSION'
      
**Ejemplo:**
    
     implementation 'com.github.Becomedigital:become_ANDROID_SDK_CEDULA_SCAN:1.0'
    
**2.**  Al realizar los pasos anteriores, debe sincronizar su proyecto con gradle.

### Inicialización de la SDK

En el método **_onCreate()_** de su clase de aplicación, inicialice Become utilizando el siguiente fragmento de código:

    public class MainActivity extends AppCompatActivity {    

    //Con el fin de manejar las respuestas de inicio de sesión, debe crear un callback utilizando el siguiente fragmento de código 
      private final BecomeCallBackManager mCallbackManager = BecomeCallBackManager.createNew ( );  
      
		     @Override  
           protected void onCreate(Bundle savedInstanceState) {  
	     super.onCreate(savedInstanceState);  
		      
		    setContentView(R.layout.activity_main);
		   
		    //Instancia para iniciar la interfaz
		    String clientSecret = "SU CLIENTSECRET AQUI";
	            String clientId = "SU CLIENID AQUI";
	            String contractId = "SU CONTRACID AQUI";

                    BecomeResponseManager.getInstance().startAutentication(MainActivity.this,
                                                                            new BDIVConfig(clientId,
                                                                                    clientSecret,
                                                                                    contractId
                                                                            ));
		    BecomeResponseManager.getInstance ( ).registerCallback (mCallbackManager, new BecomeInterfaseCallback( ) {  
          
                             @Override  
    		             public void onSuccess(final InfoDNI responseIV) {  
    		                    TextView textResponse = findViewById(R.id.textResponse);  
    		                    textResponse.setText(responseIV.toString());  
    		                    Log.d ("responseIV", responseIV.toString());  
    		              }  
			      
    		             @Override  
    		             public void onCancel() {}  
			     
    		             @Override  
    		             public void onError(LoginError pLoginError) {  
    		                  Log.d ("Error", pLoginError.getMessage ( ));  
    		             } 
			     
		         });   
           }

### Estructura encargada de la definición del estado de validación _cancelado_ por el usuario

	@Override  
	public void onCancel() { 
	    textResponse.setText ("Cancelado por el usuario ");  
	}

### Estructura encargada de la definición del estado de validación _error_

Este estado se presenta cuándo ocurren errores generales o de inicialización de parámetros:

	 @Override  
	 public void onError(LoginError pLoginError) {
	    Log.d ("Error", pLoginError.getMessage ( ));
	 }

### Estructura para el retorno de la información

La información retornará en formato JSON, el cual se encontrará conformado por los siguientes campos:

	String primerApellido;
	String segundoApellido="";
	String primerNombre="";
	String segundoNombre="";
	String cedula="";
	String rh="";
	String fechaNacimiento="";
	String sexo="";
	String fechaExpedicion = "";
	String lugarExpedicion = "";
	String estado = "";
	String resolucion = "";
	String fechaResolucion = 
	String fechaConsulta = "";
	String fuenteFallo = "";
	String numeroDocumento = "";
	String tipoDocumento = "";
	String pais = "";
	float tipoNombre = 0;
	

        toString() {
                return "InfoTarjeta{" +
                        "primerApellido='" + primerApellido + '\'' +
                        ", segundoApellido='" + segundoApellido + '\'' +
                        ", primerNombre='" + primerNombre + '\'' +
                        ", segundoNombre='" + segundoNombre + '\'' +
                        ", cedula='" + cedula + '\'' +
                        ", rh='" + rh + '\'' +
                        ", fechaNacimiento='" + fechaNacimiento + '\'' +
                        ", sexo='" + sexo + '\'' +
                        ", fechaExpedicion='" + fechaExpedicion + '\'' +
                        ", lugarExpedicion='" + lugarExpedicion + '\'' +
                        ", estado='" + estado + '\'' +
                        ", resolucion='" + resolucion + '\'' +
                        ", fechaResolucion='" + fechaResolucion + '\'' +
                        ", fechaConsulta='" + fechaConsulta + '\'' +
                        ", fuenteFallo='" + fuenteFallo + '\'' +
                        ", numeroDocumento='" + numeroDocumento + '\'' +
                        ", tipoDocumento='" + tipoDocumento + '\'' +
                        ", pais='" + pais + '\'' +
                        ", tipoNombre='" + tipoNombre + '\'' +
                '}';
             }
	}

### Requerimientos

* Tecnologias
	Android 5 en adelante
