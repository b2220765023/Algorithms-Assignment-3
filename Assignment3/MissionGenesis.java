import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filename));
            doc.getDocumentElement().normalize();

            NodeList humanMolecules = doc.getElementsByTagName("HumanMolecularData");
            molecularDataHuman = extractData(humanMolecules);

            NodeList vitalesMolecules = doc.getElementsByTagName("VitalesMolecularData");
            molecularDataVitales = extractData(vitalesMolecules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to extract molecular data from NodeList and populate MolecularData instance

    private MolecularData extractData(NodeList molecules) {
        List<Molecule> moleculeList = new ArrayList<>();
        for (int i = 0; i < molecules.getLength(); i++) {
            Node node = molecules.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList moleculeNodeList = element.getElementsByTagName("Molecule");
                for (int j = 0; j < moleculeNodeList.getLength(); j++) {
                    Element moleculeElement = (Element) moleculeNodeList.item(j);
                    String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                    int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
                    NodeList bonds = moleculeElement.getElementsByTagName("Bonds").item(0).getChildNodes();
                    List<String> bondIds = new ArrayList<>();
                    for (int k = 0; k < bonds.getLength(); k++) {
                        Node bondNode = bonds.item(k);
                        if (bondNode.getNodeType() == Node.ELEMENT_NODE) {
                            String bondId = bondNode.getTextContent();
                            bondIds.add(bondId);
                            // Add the bond in reverse direction as well
                            addReverseBond(id, bondId, moleculeList);
                        }
                    }
                    moleculeList.add(new Molecule(id, bondStrength, bondIds));
                }
            }
        }
        return new MolecularData(moleculeList);
    }

    private void addReverseBond(String id, String bondId, List<Molecule> moleculeList) {
        for (Molecule molecule : moleculeList) {
            if (molecule.getId().equals(bondId)) {
                molecule.addBond(id);
                return;
            }
        }
    }



}
