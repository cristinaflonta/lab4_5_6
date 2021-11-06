package ro.ubb.exam.Repository.XMLRepository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.InMemoryRepository;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Optional;

public class ClientXmlRepository extends InMemoryRepository <Long, Client> {
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private TransformerFactory transformerFactory;
    private Transformer transformer;
    private String filePath;

    public ClientXmlRepository(Validator<Client> validator, String filePath) {
        super(validator);
        this.filePath=filePath;
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(filePath);
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            loadData();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadData() {
        Element root = document.getDocumentElement();       //citire lista Client,
        NodeList clientNodeList = root.getChildNodes();
        for (int i=0; i<clientNodeList.getLength(); i++){
            Node clientNode = clientNodeList.item(i);
            if (!(clientNode instanceof Element)){ // verificare consistenta fisier xml
                continue;
            }
            Element clientElement = (Element) clientNode;
            buildClientFromElement(clientElement);
        }
    }

    private void buildClientFromElement(Element clientElement) {
        Long id = Long.parseLong(getTextFromTagName(clientElement, "id"));
        String name = getTextFromTagName(clientElement, "name");
        String email = getTextFromTagName(clientElement, "email");
        Client client = new Client(name, email);
        client.setId(id);
        super.save(client);
    }

    private String getTextFromTagName(Element clientElement, String tagName) {
        return clientElement.getElementsByTagName(tagName).item(0).getTextContent();

    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> clientOptional = super.save(entity);
        saveToXml(entity);

        return clientOptional;
    }

    private void saveToXml(Client entity) {
        try {
            Element root = document.getDocumentElement();
            Node node = createNodeFromClient(entity);
            root.appendChild(node);
            transformer.transform(new DOMSource(document),new StreamResult(new File(filePath)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Element createNodeFromClient(Client entity) {
        Element clientElement = document.createElement("client");
        addChildWithTextContent(clientElement, "id", String.valueOf(entity.getId()));
        addChildWithTextContent(clientElement, "name", entity.getName());
        addChildWithTextContent(clientElement, "email", entity.getEmail());
        return clientElement;
    }

    private void addChildWithTextContent(Element clientElement, String tagName, String valueOf) {
        Element childElement = document.createElement(tagName);
        childElement.setTextContent(valueOf);
        clientElement.appendChild(childElement);
    }

    @Override
    public Optional<Client> delete(Long aLong) {
        Optional<Client> clientOptional =  super.delete(aLong);
        NodeList nodeList = document.getElementsByTagName("id");
        removeClientFromDom(aLong, nodeList);
        try {
            transformer.transform(new DOMSource(document),new StreamResult(new File(filePath)));
        }catch (Exception e){
            e.printStackTrace();
        }


        return clientOptional;
    }


    private void removeClientFromDom(Long aLong, NodeList nodeList) {
        for (int i=0; i< nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            if (!(node instanceof Element)){
                continue;
            }
            Long id = Long.parseLong(node.getTextContent());
            if (aLong.equals(id)){
                node.getParentNode().getParentNode().removeChild(node.getParentNode());
            }
        }
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException {
        Optional<Client> clientOptional = super.update(entity);
        NodeList nodeList = document.getElementsByTagName("id");
        removeClientFromDom(entity.getId(), nodeList);
        saveToXml(entity);
        return  clientOptional;
    }
}

