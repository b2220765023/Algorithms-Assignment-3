import java.util.*;

public class MissionSynthesis {

    private final List<MolecularStructure> humanStructures;
    private final ArrayList<MolecularStructure> diffStructures;

    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        List<Molecule> humanMolecules = extractWeakestBonds(humanStructures);
        List<Molecule> vitalesMolecules = extractWeakestBonds(diffStructures);

        List<Molecule> allMolecules = new ArrayList<>();
        allMolecules.addAll(humanMolecules);
        allMolecules.addAll(vitalesMolecules);

        Collections.sort(allMolecules, Comparator.comparingDouble(Molecule::getBondStrength));
        for (int i=1;i< allMolecules.size();i++){
            String strCurrentID = allMolecules.get(i).getId().substring(1);
            String strMinID = allMolecules.get(0).getId().substring(1);

            int currentID = Integer.parseInt(strCurrentID);
            int minID = Integer.parseInt(strMinID);

            if(minID < currentID){
                double weight =(allMolecules.get(0).getBondStrength()+allMolecules.get(i).getBondStrength())/2.0;
                Bond bond = new Bond(allMolecules.get(i),allMolecules.get(0),weight);
                serum.add(bond);
            } else {
                double min = allMolecules.get(0).getBondStrength();
                double current = allMolecules.get(i).getBondStrength();
                double weight =(min + current)/2.0;
                Bond bond = new Bond(allMolecules.get(0),allMolecules.get(i),weight);
                serum.add(bond);
            }
        }
        return serum;
    }
    private List<Molecule> extractWeakestBonds(List<MolecularStructure> structures) {
        List<Molecule> molecules = new ArrayList<>();
        for (MolecularStructure structure : structures) {
            Molecule molecule = structure.getMoleculeWithWeakestBondStrength();
            molecules.add(molecule);
        }
        return molecules;
    }
    public void printSynthesis(List<Bond> serum) {
        System.out.println("Typical human molecules selected for synthesis: " +getMoleculeIds(humanStructures));
        System.out.println("Vitales molecules selected for synthesis: " + getMoleculeIds(diffStructures));
        System.out.println("Synthesizing the serum...");
        double totalBondStrength = 0;
        for (Bond bond : serum) {
            totalBondStrength += bond.getWeight();
            System.out.printf("Forming a bond between %s - %s with strength %.2f%n",
                    bond.getMolecule1Id(), bond.getMolecule2Id(), bond.getWeight());
        }
        System.out.printf("The total serum bond strength is %.2f%n", totalBondStrength);
    }

    private List<String> getMoleculeIds(List<MolecularStructure> structures) {
        List<String> ids = new ArrayList<>();
        for (MolecularStructure structure : structures) {
            Molecule molecule = structure.getMoleculeWithWeakestBondStrength();
            if (molecule != null) {
                ids.add(molecule.getId());
            }
        }
        return ids;
    }
}
