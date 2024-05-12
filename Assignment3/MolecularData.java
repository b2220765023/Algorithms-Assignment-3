import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        List<MolecularStructure> structures = new ArrayList<>();
        Map<String, MolecularStructure> moleculeToStructureMap = new HashMap<>();

        for (Molecule molecule : molecules) {
            if (moleculeToStructureMap.containsKey(molecule.getId())) {
                continue;
            }

            MolecularStructure newStructure = new MolecularStructure();
            assignMoleculeToStructure(newStructure, molecule, moleculeToStructureMap);
            structures.add(newStructure);
        }

        return structures;
    }

    private void assignMoleculeToStructure(MolecularStructure structure, Molecule molecule, Map<String, MolecularStructure> moleculeToStructureMap) {
        structure.addMolecule(molecule);
        moleculeToStructureMap.put(molecule.getId(), structure);

        for (String bondId : molecule.getBonds()) {
            Molecule bondedMolecule = findMoleculeById(bondId);
            if (bondedMolecule != null && !moleculeToStructureMap.containsKey(bondId)) {

                assignMoleculeToStructure(structure, bondedMolecule, moleculeToStructureMap);
            }
        }
    }

    private Molecule findMoleculeById(String id) {
        for (Molecule molecule : molecules) {
            if (molecule.getId().equals(id)) {
                return molecule;
            }
        }
        return null;
    }



    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        
        /* YOUR CODE HERE */

        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");

        for (int i = 0; i < molecularStructures.size(); i++) {
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + molecularStructures.get(i).toString());
        }
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targeStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        
        /* YOUR CODE HERE */

        for (MolecularStructure targetStructure : targeStructures.toArray(new MolecularStructure[0])) {
            boolean found = false;
            for (MolecularStructure sourceStructure : sourceStructures) {
                if (sourceStructure.equals(targetStructure)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                anomalyList.add(targetStructure);
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (int i = 0 ; i < molecularStructures.size() ; i++){
            System.out.println(molecularStructures.get(i).toString());
        }

    }
}


