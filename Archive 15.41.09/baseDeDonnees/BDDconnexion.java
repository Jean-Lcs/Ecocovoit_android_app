import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BDDconnexion {

	/* Connexion à la base de données */
	
	String url = "jdbc:mysql://localhost:port number/bdd_name"; 
	String utilisateur = "java";
	String motDePasse = "XXXX";
	
	Connection connexion = null;
	Statement statement = null;
	ResultSet resultat = null;
	
			{
	
	try {
		System.out.println("Connexion à la base de données..." );
	    connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
	    System.out.println("Connexion réussie !" );
	    
	    /* Création de l'objet gérant les requêtes */
	    Statement statement = connexion.createStatement();
	    System.out.println("Objet crée!" );
	    
	    /* Les requêtes vers la BDD */
	    
	    
	    /* Exécution d'une requête de lecture */
	    ResultSet resultat = statement.executeQuery( "SELECT idUsers, login, password, name,email  FROM Users;" );
	    System.out.println("lecture réussie !" );
	    
	    /* Récupération des données du résultat de la requête de lecture */
	    while ( resultat.next() ) {
	        int idUtilisateur = resultat.getInt( "idUsers" );
	        String loginUtilisateur = resultat.getString("login");
	        String emailUtilisateur = resultat.getString( "email" );
	        String motDePasseUtilisateur = resultat.getString( "password" );
	        String nomUtilisateur = resultat.getString( "name" );
	        
	        System.out.println("Données lu par la requête : idUsers="+idUtilisateur+",login="+loginUtilisateur+",email="+emailUtilisateur+",password="+motDePasseUtilisateur+",name="+nomUtilisateur+"");

	        /* Traiter ici les valeurs récupérées. */
	    }
	    
	    
	    
	    /* Exécution d'une requête d'écriture */
	 /*  exemple :   int statut = statement.executeUpdate( "INSERT INTO Utilisateur (email, mot_de_passe, nom, date_inscription) VALUES ('jmarc@mail.fr', MD5('lavieestbelle78'), 'jean-marc', NOW());" );
	    
	    */
	    
	    
	    
	    
	    
	} catch ( SQLException e ) {
		
	    /* "No suitable driver" :Si le driver n'a pa été chargé ou si l'URL n'est pas reconnue
	     * "Connection refused" ou "Connection timed out" si la BDD n'est pas joignable
	     * Gérer les éventuelles erreurs ici */
		
	} finally {
	    if ( connexion != null )
	        try {
	            /* Fermeture de la connexion */
	            connexion.close();
	        } catch ( SQLException ignore ) {
	            /* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
	        }
	    
			}
	
	}
}
