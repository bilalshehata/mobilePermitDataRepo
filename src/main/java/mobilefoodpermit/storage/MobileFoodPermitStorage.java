package mobilefoodpermit.storage;

import mobilefoodpermit.models.MobileFoodPermit;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.kdtree.KdNode;
import org.locationtech.jts.index.kdtree.KdTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class acts as an in memory storage system for mobile food permits
 * the implementation relies on a KdTree which is a
 * data structure that is optimised for Range queries on spatial data
 */
public class MobileFoodPermitStorage implements KdTreeStorage<MobileFoodPermit> {
    private static final Integer MAXIMUM_DISTANCE = Integer.MAX_VALUE;

    private KdTree treeStorage;

    public MobileFoodPermitStorage() {
        //if initialization becomes a bottleneck then create own version of this class.
        //Kd tree cannot be initialised with data and needs to be loaded seperatly,  StorageInitialiser.
        treeStorage = new KdTree();
    }

    /**
     * insert a permit into memory
     * This method runs syncronised as the underlying data structure implementation
     * does not support multiple concurrent writes.
     *
     * @param permit
     * @return
     */
    synchronized public MobileFoodPermit insert(MobileFoodPermit permit) {
        //check if there is already a permit at the location
        KdNode node = getNodeFromPermit(permit);
        //if there is a permit at the coordinate then append the permit to the data of that location
        if (node != null) {
            List<MobileFoodPermit> nodeData = (List<MobileFoodPermit>) node.getData();
            nodeData.add(permit);
        }
        // if there is not a permit at coordinate create a list for the current and future permits ot be added to.
        if (node == null) {
            List newList = new ArrayList<>();
            newList.add(permit);
            treeStorage.insert(new Coordinate(permit.getX(), permit.getY()), newList);
        }
        return permit;
    }

    /**
     * gets a node from the tree based on a permit
     *
     * @param permit
     * @return
     */
    private KdNode getNodeFromPermit(MobileFoodPermit permit) {
        return treeStorage.query(new Coordinate(permit.getX(), permit.getY()));
    }

    /**
     * @return all nodes in the tree
     */
    public List<KdNode> getAllNodes() {
        //create a bounding box of maximum size and search the entire tree to pull out the nodes.
        Envelope boundingBox = new Envelope(new Coordinate(0, 0));
        boundingBox.expandBy(MAXIMUM_DISTANCE);
        List<KdNode> allNodes = treeStorage.query(boundingBox);
        return allNodes;
    }

    /***
     * retrieves all permits from all nodes.
     * @return all permits in memory
     */
    public List<MobileFoodPermit> getAllPermits() {
        return dataFromNodes(getAllNodes());
    }

    /***
     * get a permit based on its locationId
     * @param locationId
     * @return
     */
    public MobileFoodPermit getByLocationId(String locationId) {
        return getAllPermits().stream()
                .filter(x -> x.getLocationId().equals(locationId))
                .findFirst().orElse(null);
    }


    /***
     * find all permits from a coordinate and a given radius.
     * all permits in the radius will be picked up
     * This function takes advantage of the tree datastructure such that the permits around a coordinate are checked
     * rather than checking all permits. This reduces time complexity from O(N^2) to (log n)
     * @param center
     * @param radius
     * @return
     */
    public List<MobileFoodPermit> getByRadius(Coordinate center, double radius) {
        // create bounding circle with given coordinate at its center
        Envelope boundingBox = new Envelope(new Coordinate(center));
        //expand the bounding circle to the size of the radius
        boundingBox.expandBy(radius);
        // query for all items within the bounding circle.
        List<KdNode> nodesInRadius = treeStorage.query(boundingBox);
        return dataFromNodes(nodesInRadius);

    }

    /***
     * retrieves a list of permits from a list of nodes in the tree.
     * @param nodes
     * @return
     */
    public List<MobileFoodPermit> dataFromNodes(List<KdNode> nodes) {

        // Unfortunately the library doesn't use generics so we will have to typecast here.
        return nodes.stream()
                .map(it -> (List<MobileFoodPermit>) it.getData())
                .filter(data -> data != null)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


}
