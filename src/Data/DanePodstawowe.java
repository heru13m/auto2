package Data;

import Controller.Auto;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Klasa mająca na celu przechowywanie przebiegow
 **
 * @version 1.0
 * @author Maciej Ksiezak
 * @author Mateusz Mus
 *
 */
public class DanePodstawowe {

    public UstawieniaSamochodu wczytajPodstawoweDane()
    {
        File fXmlFile = new File("data.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(fXmlFile);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();

        float przebieg = Float.valueOf(doc.getElementsByTagName("przebieg").item(0).getTextContent());
        float przebieg1 = Float.valueOf(doc.getElementsByTagName("przebieg1").item(0).getTextContent());
        float przebieg2 = Float.valueOf(doc.getElementsByTagName("przebieg2").item(0).getTextContent());


        return new UstawieniaSamochodu(przebieg,przebieg1,przebieg2);

    }

    public boolean zapiszUstawienia(Auto auto)
    {
        try {
            File fXmlFile = new File("data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = null;

            try {
                doc = dBuilder.parse(fXmlFile);
                System.out.print("");
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            doc.getDocumentElement().normalize();

            doc.getElementsByTagName("przebieg").item(0).setTextContent(Float.toString(auto.getPrzebiegCalkowity()));
            doc.getElementsByTagName("przebieg1").item(0).setTextContent(Float.toString(auto.getPrzebieg1()));
            doc.getElementsByTagName("przebieg2").item(0).setTextContent(Float.toString(auto.getPrzebieg2()));


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //write to console or file
            StreamResult file = new StreamResult(new File("data.xml"));

            //write data
            transformer.transform(source, file);


            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }


    }

}