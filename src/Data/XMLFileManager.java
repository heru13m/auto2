//package Data;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import com.thoughtworks.xstream.XStream;
//
///**
// * Klasa umożliwiająca obsługę zapisu i odczytu obiektów z plików XML.
// *
// * @author Adam Kalisza
// * @author Kamil Rojszczak
// */
//public class XMLFileManager implements FileManagerInterface {
//
//	private XStream xs;
//	private String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
//
//	/**
//	 * Konstruktor, tworzy obiekt klasy XStream.
//	 */
//	public XMLFileManager() {
//		this.xs = new XStream();
//	}
//
//	/**
//	 * Zapisuje obiekt do pliku podanego w ścieżce za pomocą biblioteki XStream.
//	 * @param obj obiekt do zapisu
//	 * @param path ścieżka zapisu pliku wraz z nazwą i rozszerzeniem
//	 * @throws IOException wyjątek nieprawidłowej operacji wejścia wyjścia
//	 */
//	@Override
//	public void saveToFile(Object obj, String path) throws IOException {
//		FileWriter ostream = new FileWriter(new File(path));
//		ostream.write(this.declaration + xs.toXML(obj));
//		ostream.close();
//	}
//
//	/**
//	 * Odczytuje obiekt z pliku XML podanego w ścieżce.
//	 * @param path ścieżka do pliku wraz z nazwą i rozszerzeniem
//	 * @return odczytany obiekt
//	 * @throws IOException wyjątek nieprawidłowej operacji wejścia wyjścia
//	 * @throws ClassNotFoundException wyjątek niepoprawnej klasy
//	 */
//	@Override
//	public Object readFromFile(String path) throws IOException, ClassNotFoundException {
//		Class<?>[] classes = new Class[] { Data.Travel.class, Data.CarSettings.class };
//		XStream.setupDefaultSecurity(xs);
//		xs.allowTypes(classes);
//
//		BufferedReader istream = new BufferedReader(new FileReader(path));
//		String xmlR = new String();
//
//		for(int i = 0; i < this.countLines(path); i++)
//			xmlR += istream.readLine() + "\n";
//		istream.close();
//		return xs.fromXML(xmlR);
//	}
//
//	/**
//	 * Liczy ilość linii w podanym pliku XML
//	 * @param path ścieżka do pliku wraz z nazwą i rozszerzeniem
//	 * @return ilość linii
//	 * @throws IOException wyjątek nieprawidłowej operacji wejścia wyjścia
//	 */
//	public int countLines(String path) throws IOException {
//		int counter = 0;
//		BufferedReader istream = new BufferedReader(new FileReader(path));
//		while (istream.readLine() != null)
//			counter++;
//		istream.close();
//		return counter;
//	}
//
//	/**
//	 * Ustawia alias dla danej klasy.
//	 * @param newName alias na który chcemy zmienić nazwę klasy
//	 * @param clas klasa dla której zmiana ma być wprowadzona
//	 */
//	public void addClassAlias(String newName, Class<?> clas) {
//		xs.alias(newName, clas);
//	}
//
//	/**
//	 * Ustawia alias dla pola danej klasy.
//	 * @param newName alias na który chcemy zmienić pole danej klasy
//	 * @param clas klasa dla której zmiana ma być wprowadzona
//	 * @param fieldName nazwa pola klasy dla którego ustawiamy alias
//	 */
//	public void addFieldAlias(String newName, Class<?> clas, String fieldName) {
//		xs.aliasField(newName, clas, fieldName);
//	}
//
//	/**
//	 * Zwraca ustawioną dekralację
//	 * @return deklaracja jako String
//	 */
//	public String getDeclaration() {
//		return declaration;
//	}
//
//	/**
//	 * Zmienia domyślną deklarację
//	 * @param declaration nowa deklaracja
//	 */
//	public void setDeclaration(String declaration) {
//		this.declaration = declaration;
//	}
//
//}
