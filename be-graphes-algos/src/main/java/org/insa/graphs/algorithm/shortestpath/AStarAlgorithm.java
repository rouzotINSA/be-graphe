package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.LabelStar;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    @Override
    protected ShortestPathSolution doRun() {
    	
    	// Data input
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        List<Node> nodes = graph.getNodes();
        final int nbNodes = graph.size();
        
        // Index Origine/Destination
        int indexOrigine = data.getOrigin().getId();
        int indexDestination = data.getDestination().getId();
        
        // Information pour tracer le chemin
        notifyOriginProcessed(data.getOrigin());

        
        // INIT
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        ArrayList<LabelStar> LabelStars = new ArrayList<LabelStar>();
        
        // On initialise tous les LabelStars à +infini, avec marque à false et pere à NULL
        for (int i = 0; i < nbNodes; i++) {
        	LabelStars.add(new LabelStar(nodes.get(i), data.getDestination(), false, Float.POSITIVE_INFINITY, null));
        }
        
        // Cour d'origine a zéro
        LabelStars.get(indexOrigine).setCost(0);
        
        // On insère le LabelStar actualisé dans le tas
        tas.insert(LabelStars.get(indexOrigine));
        
        // Nombre d'itérations
        int nbIterations = 0;
        
        
        // On boucle tant que la destination n'a pas été visitée et qu'il reste des LabelStars dans le tas 
        while (!LabelStars.get(indexDestination).estMarque() && tas.size() != 0) {
        	// On récupère le LabelStar minimal dans le tas
        	Label LabelStar_min = tas.deleteMin();
        	// On marque le LabelStar minimal
        	LabelStars.get(LabelStar_min.getSommet().getId()).marquer();
        	
        	// Debug
        	System.out.println("Coût du LabelStar : " + LabelStar_min.getCost());
        	System.out.println("Taille du tas : " + tas.size());
        	nbIterations++;
        	
        	// On récupère les arcs successeurs du LabelStar minimal
        	List<Arc> arcs = LabelStar_min.getSommet().getSuccessors();
        	
        	// Debogage
        	System.out.println("Nombre de successeurs du LabelStar : " + arcs.size());
        	
        	for (int i = 0; i < arcs.size(); i++) {
        		// On vérifie que le chemin est autorisé
                if (!data.isAllowed(arcs.get(i))) {
                    continue;
                }
                // On récupère l'index de la destination de l'arc actuel
        		int index_suiv = arcs.get(i).getDestination().getId();
        		
        		if (!LabelStars.get(index_suiv).estMarque())
        		{
        			double oldDistance = LabelStars.get(index_suiv).getCost();
        			double newDistance = LabelStar_min.getCost() + data.getCost(arcs.get(i));
        			
        			// On update le tracé sur la carte à chaque Node explorée
        			if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(arcs.get(i).getDestination());
                    }
        			
        			if (newDistance < oldDistance) {
        				LabelStars.get(index_suiv).setCost(newDistance);
        				LabelStars.get(index_suiv).setPere(arcs.get(i));
        				if (Double.isFinite(oldDistance)) {
        					tas.remove(LabelStars.get(index_suiv));
        				}
        				tas.insert(LabelStars.get(index_suiv));
        			}
        		}
        	}
        }
        
        // Solution
        ShortestPathSolution solution = null;
        
        //La destination n'a pas de prédécesseur, le chemin est infaisable
        if (!LabelStars.get(indexDestination).estMarque()) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            //La destination a été trouvée. On en informe l'utilisateur.
            notifyDestinationReached(data.getDestination());

            //On crée un nouveau chemin à partir des prédécesseurs
            ArrayList<Arc> chemin = new ArrayList<>();
            Arc arc = LabelStars.get(indexDestination).getPere();
            while (arc != null) {
                chemin.add(arc);
                arc = LabelStars.get(arc.getOrigin().getId()).getPere();
            }
            
            System.out.println("Nombre d'itérations : " + nbIterations);
            System.out.println("Nombre d'arcs : " + chemin.size());
            
            //On inverse ce chemin
            Collections.reverse(chemin);

            //On crée la solution finale
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, chemin));
            
            // Debug
            if (!solution.getPath().isValid()) {
            	System.out.println("Le chemin n'est pas valide !");
            }
            else {
            	System.out.println("Solution trouvée !");
            }
            
            System.out.println("Durée : " + solution.getPath().getMinimumTravelTime() + "Longueur : " + solution.getPath().getLength() + ", Coût Dijkstra : " + LabelStars.get(indexDestination).getCost());
            
        }
        return solution;
    }
}
