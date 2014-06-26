import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Time;

import de.shaoranlaos.dbAPI.Gruppe;
import de.shaoranlaos.dbAPI.Person;
import de.shaoranlaos.dbAPI.Tauschanfrage;
import de.shaoranlaos.dbAPI.Termin;

public interface ServerInterface extends Remote{
	
	int erstellenPerson(String Nutzername, String Passwort, String Email, String Status, String Nachname, String Vorname, String Geburtstag,int Gruppe ) throws RemoteException ;
	Person anmeldenNutzer(String Nutzername,String Passwort)throws RemoteException ;
	Person[] anzeigenPersonen()throws RemoteException ;
	Person anzeigenPerson(int PersonenID)throws RemoteException ;
	boolean hinzufuegenMitglied(int PersonID,String GruppenID)throws RemoteException ;  //Hinzufuegen einer Person zur eigenen Gruppe
	boolean loeschenMitglied(int PersonID)throws RemoteException ;                     //Entfernen eines Mitgliedes aus der eigenen Gruppe
	boolean loeschenPerson(int PersonID)throws RemoteException ;                        //Loeschen einer Person aus der DB
	boolean vergebenRechte(int PersonID, int rechte)throws RemoteException ;
	Person[] anzeigenMitglieder(int GruppenID)throws RemoteException ;
	int erstellenGruppe(String Gruppenname, int GL_ID) throws RemoteException;
    boolean aendernGruppenleiter(int GruppenID, int PersonID) throws RemoteException;
    public boolean aendernGruppenname(int Gruppen_ID, String Gruppenname) throws RemoteException;
	boolean loeschenGruppe(int GruppenID) throws RemoteException ;
	int ueberspringenTermin(int TerminID) throws RemoteException ; 
	public boolean aussetzenTermin() throws RemoteException ; 
	boolean aussetzenTermin(int TerminID) throws RemoteException ;
	int erstellenTauschanfrage(int TerminIDQuelle, int TerminIDZiel) throws RemoteException ;
	//boolean hinzufuegenTermin(Termin termin)throws RemoteException ; 
	boolean aendernTermin(int TerminID, java.sql.Date Datum, java.sql.Time zeit, String ort, String essen) throws RemoteException;
	int einfuegenAuZykTermin(int GruppenID, Date Datum,Time zeit,String ort,String essen)throws RemoteException;
	public Termin[] anzeigenTermine() throws RemoteException;
	public Termin anzeigenTermin(int TerminID)throws RemoteException;
	boolean loeschenAbHier(String Datum)throws RemoteException ;
	boolean einfuegenAbHier()throws RemoteException ;
	boolean vergebenNick(String nickname)throws RemoteException ;
	boolean aendernPasswort(int PersonenID, String Passwort)throws RemoteException ;
	boolean pruefenPasswort(int PersonenID, String Passwort)throws RemoteException ;
	Gruppe anzeigenGruppe(int GruppenID)throws RemoteException ;
	Gruppe[] anzeigenGruppen()throws RemoteException ;
	boolean aendernNickname(int PersonenID, String Nickname)throws RemoteException ;
	boolean aendernVorname(int PersonenID, String Vorname) throws RemoteException ;
	boolean aendernNachname(int PersonenID, String Nachname)throws RemoteException ;
	boolean aendernEmail(int PersonenID, String Email)throws RemoteException ;
	boolean aendernGebDat(int PersonenID, String GebDat)throws RemoteException ;
	//boolean aendernGruppe(int PersonenID, String GroupID)throws RemoteException ;  aufgedroeselt in: hinzfuegenMitglied bzw loeschenMitglied
	boolean aendernReihenfolge(int GID, String Reihenfolge)throws RemoteException;
	public boolean existTauschanfrage(int TerminID) throws RemoteException;
	public int insertTauschanfrage(int TerminID_alt, int TerminID_neu) throws RemoteException;
	public Tauschanfrage[] anzeigenTauschanfragenGruppe(int GruppenID) throws RemoteException;
	public boolean executeTauschanfrage(int idTausch) throws RemoteException;
	public boolean deleteTauschanfrage(int idTausch) throws RemoteException;
	Termin anzeigenTermin(Date datum)throws RemoteException;
	Person[] anzeigenNichtMitglied()throws RemoteException;
	/* Person, Termin: 
	 * Klassen die noch angelegt werden muss
	 */
	 
	 /*Client-Methoden
	 void anmeldenClient()
	 void erstellenNutzer()
	 void loeschenNutzer()
	 void aendernNickname()
	 void hinzufuegenMitglied() : server.aendernGruppe
	 void erstellenGruppe()
	 void aendernGruppenleiter()
	 void loeschenGruppe()
	 boolean weiterAendern()
	 void aendernTermin()
	 void aendernPasswort()
	 void anzeigenTermin()
	 Personenwerte aendern(Name,Vorname,B-Day,E-mail,nickname)
	 public void aendernReihenfolge()
	 */

}


/* INFOS ZU DEN KLASSEN  AUS DBAPI: ...

public class Person {
	public int id;
	public String passwort;
	public String email;
	public String nickname;
	public String nachname;
	public String vorname;
	public int gruppe;
	public Date geburtstag;
	public int rechte;
}

public class Gruppe {
	public int id; 
	public String name;
	public int leiter;
	public int reihenfolge;
}

public class Tauschanfrage {
	public int id;
	public int alt;
	public int neu;
}

public class Termin {
	public int id;
	public int gruppe;
	public String ort;
	public String essen;
	public boolean istgetauscht;
	public Date datum;
	public Time zeit;
	public Timestamp stempel;
}


*/





















