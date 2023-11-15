package controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//Mantida caso sejam necessários os métodos de serialização
public class Singleton implements Serializable{
		
	private static final long serialVersionUID = 107634094355526964L;
	protected volatile Singleton instancia;
	
	public Singleton deserializar(String arq) throws IOException, ClassNotFoundException{
		Singleton instancia;
		
		FileInputStream config = new FileInputStream(new File(arq));
		ObjectInputStream obj = new ObjectInputStream(config);
		
		instancia = (Singleton) obj.readObject();
		
		obj.close();
		config.close();
		
		return instancia;
	}
		
	private Singleton(){}
	
	public Singleton obterInstancia(String arq) {
        if (instancia == null) {
            synchronized (Singleton.class) {
                if (instancia == null) {
                	try{
                		instancia = (Singleton) deserializar(arq);
                	} catch(IOException | ClassNotFoundException e){
                		instancia = new Singleton();
                	}                	
                }
            }
        }

        return instancia;
    }
	
	public void salvarInstancia(String arq){
		try {
			FileOutputStream out = new FileOutputStream(arq);
			ObjectOutputStream st = new ObjectOutputStream(out);
			
			st.writeObject(instancia);
			
			st.close();
			out.close();
		} catch (IOException e) {
			
		}
	}
}
