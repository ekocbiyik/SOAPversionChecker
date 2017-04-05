package net.egemsoft.utils;

import org.apache.log4j.Logger;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by enbiya on 20.03.2017.
 */
public class PostNewVersion {

    static Logger logger = Logger.getLogger(PostNewVersion.class);

    public static boolean sendVersion(String key, String value) {

        boolean soapResponseResult = false;

        logger.info(key + "- " + value + " will be posted..");
        try {

            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = null;
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();


            // Send SOAP Message to SOAP Server
            String url = "http://ekocbiyik.com:801/Admin/WebService/RMVersionManager.asmx";
            String ortam = ""; //TEST/REGRESYON/HOTFIX
            String app = ""; //MTS/Netyüz/IYS
            String version = value;

            if (key.contains("MTS")) {
                app = "MTS";
            } else if (key.contains("NETYUZ")) {
                app = "Netyüz";
            } else {
                app = "IYS";
            }

            if (key.contains("TEST")) {
                ortam = "TEST";
            } else if (key.contains("REG")) {
                ortam = "REGRESYON";
            } else {
                ortam = "HOTFIX";
            }

            SOAPMessage soapRequest = createSOAPRequest(ortam, app, version);
            if (soapRequest == null) {
                logger.info("An error occured while prepairing to soap request!");
                return false;
            }

            logger.info("Soap request is sending for ortam: " + ortam + ", app: " + app + ", version: " + version);
            SOAPMessage soapResponse = soapConnection.call(soapRequest, url);


            SOAPBody body = soapResponse.getSOAPBody();
            Iterator updates = body.getChildElements();
            while (updates.hasNext()) {

                // The listing and its ID
                SOAPElement update = (SOAPElement) updates.next();
                String status = update.getAttribute("VersionUpdateResponse");
                logger.info("Status: " + status);

                Iterator i = update.getChildElements();
                while (i.hasNext()) {
                    SOAPElement e = (SOAPElement) i.next();
                    String name = e.getLocalName();
                    String value1 = e.getValue();
                    logger.info("SOAP Response result: " + name + "=" + value1);
                    if (name.equalsIgnoreCase("VersionUpdateResult") && value1.equalsIgnoreCase("0")) {
                        soapResponseResult = true;
                        logger.info("soap request is succesfull");
                        break;
                    }

                }
            }

            // Process the SOAP Response
            printSOAPResponse(soapResponse);
            soapConnection.close();

        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return soapResponseResult;
        }

    }

    private static SOAPMessage createSOAPRequest(String ortam, String app, String version) {

        try {

            MessageFactory messageFactory = null;
            messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            String serverURI = "http://ekocbiyik.com/Admin/WebService/";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("web", serverURI);


            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("VersionUpdate", "web");

            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("Domain", "web");
            soapBodyElem1.addTextNode("GENISBANT");

            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("Ortam", "web");
            soapBodyElem2.addTextNode(ortam);//TEST/REGRESYON/HOTFIX

            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("App", "web");
            soapBodyElem3.addTextNode(app);//iys netyuz

            SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("Email", "web");
            soapBodyElem4.addTextNode("admin@ekocbiyik.com");

            SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("Version", "web");
            soapBodyElem5.addTextNode(version);//bursaı version no

            SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("DeployReason", "web");
            soapBodyElem6.addTextNode("Yeni Gelistirme");

            MimeHeaders headers = soapMessage.getMimeHeaders();

            headers.addHeader("SOAPAction", serverURI + "VersionUpdate");

            soapMessage.saveChanges();

            /* Print the request message */
            soapMessage.writeTo(System.out);

            return soapMessage;

        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        logger.info("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);

    }

}
