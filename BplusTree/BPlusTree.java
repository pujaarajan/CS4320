import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.ArrayList;



/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 */
public class BPlusTree<K extends Comparable<K>, T> {

	public Node<K,T> root;
	public static final int D = 2;

	/**
	 * TODO Search the value for a specific key
	 * 
	 * @param key
	 * @return value
	 */
	public T search(K key) {

		return sTraverse(key, root);
	}


	private T sTraverse(K key, Node cNode){
		if( cNode.isLeafNode == true){
			LeafNode<K,T> leaf = (LeafNode) cNode;
			for(int i = 0; i< leaf.keys.size(); i++ ){
				if( leaf.keys.get(i)== key){
					return leaf.values.get(i);
				}
			}
			return null;
		}
		else{
			IndexNode<K,T> node = (IndexNode) cNode;
			for(Node childNode: node.children ){
				int len = childNode.keys.size();
				if (  ((K) (childNode.keys.get(0))).compareTo(key) !=0 && ((K)(childNode.keys.get(len-1)) ).compareTo(key) >0){
					return sTraverse(key, childNode);
				} 
			}

		}
		return null;
	}



	/**
	 * TODO Insert a key/value pair into the BPlusTree
	 * 
	 * @param key
	 * @param value
	 */
	public void insert(K key, T value) {
		if (root == null){
			root= new LeafNode(key,value);
			((LeafNode)root).previousLeaf = ((LeafNode)root);
			((LeafNode)root).nextLeaf= ((LeafNode)root);
		}
		else if ( root.isLeafNode==true){
		  AbstractMap.Entry<K, Node<K,T>> result =  insertHelp(key,value, root);
		  if (result.getKey() != null){
		  	root = new IndexNode( result.getKey() ,root, ((Node<K,T>) result.getValue()) );
		  }
		}
		else{
			insertHelp(key,value,root);
		}
	}

	

	public Entry<K,Node<K,T>> insertHelp(K key, T value, Node<K,T> n){

		int i = 0;
		if(n.isLeafNode){
			for(K fKey : n.keys){
				if(fKey.compareTo(key)>0 ){
					n.keys.add(i,key);
					((LeafNode) n).values.add(i,value);
					if( n.isOverflowed()){
						return splitLeafNode( (LeafNode) n);
					}
					return (new AbstractMap.SimpleEntry<K, Node<K,T>>(null,null));
				}
				else if (i==n.keys.size()-1) {
					n.keys.add(key);
					((LeafNode) n).values.add(value);
					if( n.isOverflowed()){	
						return splitLeafNode( (LeafNode) n);
					}
					return (new AbstractMap.SimpleEntry<K, Node<K,T>>(null,null));	
				}
				i++;
			}
		}
		else{
			for(K nKey : n.keys){
				i++;
				if(nKey.compareTo(nKey)>0){
					AbstractMap.SimpleEntry<K, Node<K,T>> entry =  ((AbstractMap.SimpleEntry) insertHelp(key,value, ((Node)((IndexNode)n).children.get(i+1)) ));
					if( entry.getKey() != null){
						 n.keys.add(i,entry.getKey());
						 ((IndexNode)n).children.add(i+1,entry.getValue());
						if( n.isOverflowed()){
							return splitIndexNode( ((IndexNode)n));
						}
					}
					return (new AbstractMap.SimpleEntry<K, Node<K,T>>(null,null));	

				}
				else if(i==n.keys.size()){
					AbstractMap.SimpleEntry<K, Node<K,T>> entry =  ((AbstractMap.SimpleEntry) insertHelp(key,value, ((Node)((IndexNode)n).children.get(i)) ));
					if( entry.getKey() != null){
						 n.keys.add(entry.getKey());
						 ((IndexNode)n).children.add(entry.getValue());
						if(n.isOverflowed()){
							return splitIndexNode( ((IndexNode)n));
						}	 
					}
					return (new AbstractMap.SimpleEntry<K, Node<K,T>>(null,null));	
				}
			}
		}
		return null;
	}



	/**
	 * TODO Split a leaf node and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param leaf
	 * @return the key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {
		int arrayCount = 0;
		ArrayList<K> keys = new ArrayList<K>();
		ArrayList<T> values = new ArrayList<T>();
		ArrayList<K> lKeys = leaf.keys;
		LeafNode rNode;
		int splitPoint = (lKeys.size()/2);
		K splitKey = lKeys.get(0);

		for(K key: lKeys){
			if( arrayCount==splitPoint) {
				keys.add(key);
				values.add(leaf.values.get(arrayCount));
				splitKey= key;
			}
			else if (arrayCount> splitPoint) {
				keys.add(key);
				values.add(leaf.values.get(arrayCount));
			}
			arrayCount++;
		}

		leaf.keys =  new ArrayList<K> (leaf.keys.subList(0, (splitPoint)));
		leaf.values =  new ArrayList<T> (leaf.values.subList(0, (splitPoint)));

		rNode = new LeafNode(keys, values);
		rNode.previousLeaf = leaf;
		rNode.nextLeaf = leaf.nextLeaf;
		leaf.nextLeaf = rNode;

		return new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rNode);
	}

	/**
	 * TODO split an indexNode and return the new right node and the splitting
	 * key as an Entry<slitingKey, RightNode>
	 * 
	 * @param index
	 * @return new key/node pair as an Entry
	 */
	public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {
		int i = 0;
		ArrayList<K> keys = new ArrayList<K>();
		ArrayList<Node<K,T>> children= new ArrayList<Node<K,T>> ();
		ArrayList<K> lKeys = index.keys;
		IndexNode<K,T> rNode;
		int splitPoint = (lKeys.size()/2);
		K splitKey = lKeys.get(0);


		for(K key: lKeys){
			if( i == splitPoint){
				splitKey = key;
			}
			else if(i>= (splitPoint+1) ) {
				keys.add(key);
				children.add(index.children.get(i));
			}
			i++;
		}

		children = new ArrayList<Node<K,T>>  (children.subList(1,(children.size()-1)));
		children.add(index.children.get((index.children.size()-1)));
		rNode = new IndexNode(keys, children );

		return new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rNode);

	}
    


	/**
	 * TODO Delete a key/value pair from this B+Tree
	 * 
	 * @param key
	 */
	public void delete(K key) {
			if(root !=null){
				if( root.isLeafNode){
					for( int i=0; i< root.keys.size() ; i++ ){
						if(root.keys.get(i) == key){
							root.keys.remove(i);
							((LeafNode)root).values.remove(i);
						}
					}
				}
				else{
					for(int i=0; i<root.keys.size(); i++){
						if(root.keys.get(i).compareTo(key)>0){
							deleteHelp(key, ((Node<K,T>) ((IndexNode)root).children.get(i)), root);
						} 
						else if (i==root.keys.size()-1){
							deleteHelp(key, ((Node<K,T>) ((IndexNode)root).children.get(i+1)),root);
					}
				}
			}
		}

}
	

	public void deleteHelp( K key, Node<K,T> n, Node<K,T> parent){
	
		int k =0;

		if (n.isLeafNode){
			for( int i=0; i< n.keys.size(); i++ ){
				if(n.keys.get(i) == key){
					n.keys.remove(i);
					((LeafNode)n).values.remove(i);
					if( n.isUnderflowed()){
						int result = handleLeafNodeUnderflow( ((LeafNode)n).previousLeaf, ((LeafNode) n), ((IndexNode) parent) );
						if(result != -1){
							parent.keys.remove(result);							
						}
					}
				}
			}
		}
	else{
		IndexNode<K,T> prevNode =null;


		for(K nKey: parent.keys){
			if( (nKey.compareTo(n.keys.get(0))) >1){
 				if(k-1==-1){
 					prevNode= ((IndexNode) ((IndexNode) parent).children.get(parent.keys.size()-1) );
					break;
 				} ; 
			
				}
			k++;
			if (k==parent.keys.size()){
				prevNode= ((IndexNode) ((IndexNode) parent).children.get( ((IndexNode) parent).children.size()-1 ));
			}
		}

		k=0;
		for(K fKey: n.keys){
			if( (fKey.compareTo(key)) >1){
				deleteHelp(key, ((Node<K,T>) ((IndexNode)n).children.get(k)), n);

	
				if(n.isUnderflowed()){ 
					int result = handleIndexNodeUnderflow (prevNode, ((IndexNode)n), ((IndexNode) parent) );
						if( result != -1 && parent!= null){
							parent.keys.remove(result);
						}
				}
			}
			k++;
		}
	}
}



	/**
	 * TODO Handle LeafNode Underflow (merge or redistribution)
	 * isUnderflowed
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
			IndexNode<K,T> parent) {


		int leftSize = left.keys.size();
		int rightSize = right.keys.size();
		int i=0;

		if ((leftSize+rightSize) <= (2*D) ){
			left.keys.addAll(right.keys);
			left.values.addAll(right.values);
			
			left.nextLeaf= right.nextLeaf;

			for(K nKey: parent.keys){
				if( (nKey.compareTo(left.keys.get(leftSize-1)))>0 ){
					return (i);
				}
				i++;
			}

		}

		int splitPoint = (D/2);

		int  sizeInd = (rightSize-1);
		int distAmount = sizeInd-splitPoint;

		for(int k=0; k< distAmount; k++ ){
			left.keys.add(right.keys.get(k));
			left.values.add(right.values.get(k));
		}

		K distKey = right.keys.get(0);
		T distVal = right.values.get(0);


		for(K fKey: parent.keys){
				if(fKey.compareTo(left.keys.get(leftSize-1))>1 ){
				parent.keys.set(i,distKey);				
			}
 		}

		right.keys =  new ArrayList<K> (right.keys.subList( splitPoint-1, sizeInd-1));
		right.values = new ArrayList<T> (right.values.subList(splitPoint-1, sizeInd-1));

		left.keys.add( distKey);
		left.values.add(distVal);

 		return -1;

	}


	

	/**
	 * TODO Handle IndexNode Underflow (merge or redistribution)
	 * 
	 * @param left
	 *            : the smaller node
	 * @param right
	 *            : the bigger node
	 * @param parent
	 *            : their parent index node
	 * @return the splitkey position in parent if merged so that parent can
	 *         delete the splitkey later on. -1 otherwise
	 */
	public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
			IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
		
		int leftSize = leftIndex.keys.size();
		int rightSize =  rightIndex.keys.size();
		int parSize = parent.keys.size();
		int parentKey =0;

		int k = 0;
		for(K nKey: parent.keys){
			if(nKey.compareTo(leftIndex.keys.get(leftSize-1))>1 ){
				parentKey = k;
				break;
			}
			k++;
		}

		if( (leftSize+rightSize+1)<=(2*D)){

			leftIndex.keys.addAll(rightIndex.keys);
			leftIndex.children.addAll( new ArrayList<Node<K,T>>  (rightIndex.children.subList( 1, (rightSize-1))  ));				
			return parentKey;
		}
		



		K distKey = rightIndex.keys.get(0);
		int splitPoint = (D/2);

		leftIndex.keys.add(parent.keys.get(parentKey));
		int  sizeInd = (rightSize-1);

		int distAmount = sizeInd-splitPoint;

		for(int i=0; i< distAmount; i++ ){
			leftIndex.keys.add(rightIndex.keys.get(i));
			leftIndex.children.add(rightIndex.children.get(i));
			rightIndex.keys.remove(0);
			rightIndex.children.remove(0);
		}

		parent.keys.set(parentKey,rightIndex.keys.get(0));
		return -1;
		}
	

}
