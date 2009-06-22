package example;

import cellml_api.CellMLBootstrap;
import cellml_api.CellMLComponent;
import cellml_api.CellMLComponentIterator;
import cellml_api.CellMLComponentSet;
import cellml_api.CellMLVariable;
import cellml_api.CellMLVariableIterator;
import cellml_api.CellMLVariableSet;
import cellml_api.Connection;
import cellml_api.ConnectionIterator;
import cellml_api.ConnectionSet;
import cellml_api.DOMModelLoader;
import cellml_api.MapComponents;
import cellml_api.MapVariables;
import cellml_api.MapVariablesIterator;
import cellml_api.MapVariablesSet;
import cellml_api.Model;
import cellml_api.RDFRepresentation;
import cellml_api.RDFXMLStringRepresentation;
import cellml_api.VariableInterface;

/**
 * Example java code which uses the CellML API to construct models.
 */
public class CellMLJavaTest{
	private CellMLBootstrap cb;
	
	public CellMLJavaTest(){
        System.out.println("CellMLJavaTest launched. Loading library...");
        System.loadLibrary("cellml_java_bridge");
		cb = cellml_bootstrap.CellMLBootstrap.createCellMLBootstrap();	
		
	}
	
    public static void main(String[] argv)    {
    	CellMLJavaTest cjtest = new CellMLJavaTest(); 
    	
    	cjtest.createCellMLModel();
    	cjtest.loadCellMLModel();
    	
    }
    
    /**
     * creating an example CellML 1.1 model with two components
     */
    private void createCellMLModel(){
        Model m = cb.createModel("1.1");
        m.setName("example1");
        
        //creating component 1
        CellMLComponent comp1 = m.createComponent(); 
        m.addElement(comp1);
        comp1.setName("component1");        
        
        //adding a variable to component 1
        CellMLVariable var1 = m.createCellMLVariable();
        comp1.addElement(var1);
        var1.setName("variable1");
        var1.setUnitsName("cm");
    	var1.setInitialValue("10");            	
    	var1.setPublicInterface(VariableInterface.INTERFACE_OUT);     	
            
        //creating component 2
        CellMLComponent comp2 = m.createComponent(); 
        m.addElement(comp2);
        comp2.setName("component2");          
        
        
        //adding a variable to component 2
        CellMLVariable var2 = m.createCellMLVariable();
        comp2.addElement(var2);
        var2.setUnitsName("cm");
      	var2.setPublicInterface(VariableInterface.INTERFACE_IN);
      	
      	
      	//connecting the two variables (Creating a connection also creates a MapComponent)
      	Connection con = m.createConnection();
      	m.addElement(con);

      	MapComponents mapComp = con.getComponentMapping();      
      	mapComp.setFirstComponent(comp1);
      	mapComp.setSecondComponent(comp2);      	
      
      	MapVariables mapvar = m.createMapVariables();
      	con.addElement(mapvar);
      	
      	mapvar.setFirstVariable(var1);
     	mapvar.setSecondVariable(var2);      	

        System.out.println(m.getSerialisedText());
    }
    
    /**
     * loading an existing CellML model
     */
    private void loadCellMLModel(){
    	DOMModelLoader modelLoader = cb.getModelLoader();
    	
    	//loading the Hodgkin Huxley model from the repository
    	Model model = modelLoader.loadFromURL("http://www.cellml.org/models/hodgkin_huxley_1952_version07/download");
     	
    	System.out.println("Model Name:" + model.getName() + "\n");
    	
    	//Iterating components and their variables
    	CellMLComponentSet componentSet = model.getModelComponents();
    	CellMLComponentIterator iter = componentSet.iterateComponents();
    	
    	for(int i = 0; i < componentSet.getLength(); i++){
    		CellMLComponent comp = iter.nextComponent();
    		System.out.println("Component Name:"+comp.getName());
    		
    		CellMLVariableSet variableSet = comp.getVariables();
    		CellMLVariableIterator varIter = variableSet.iterateVariables();
    		
    		for(int j = 0; j < variableSet.getLength(); j ++){
    			CellMLVariable variable = varIter.nextVariable();
    			System.out.println("Variable Name:"+variable.getName());
    		}
    		System.out.println();
    	}
    	
    	//Iterating connections
    	ConnectionSet connectionSet = model.getConnections();
    	ConnectionIterator conIter = connectionSet.iterateConnections();
    	
    	for(int i = 0; i < connectionSet.getLength(); i++){
    		Connection connection = conIter.nextConnection();
    		MapComponents mapComponent = connection.getComponentMapping();
    		System.out.println("Connection Component1: "+mapComponent.getFirstComponentName() + "    Component2: " + mapComponent.getSecondComponentName());
    		
    		MapVariablesSet variableSet = connection.getVariableMappings();
    		MapVariablesIterator varIter = variableSet.iterateMapVariables();
    		
    		for(int j =0; j < variableSet.getLength(); j++){
    			MapVariables mapVar = varIter.nextMapVariable();
        		System.out.println("\t Variable1: "+mapVar.getFirstVariableName() + "    Variable2: " + mapVar.getSecondVariableName());
    		}
    		System.out.println();
    	}
    	
    	RDFRepresentation rep = model.getRDFRepresentation("http://www.cellml.org/RDFXML/string");
//    	RDFXMLStringRepresentation stringRep = (RDFXMLStringRepresentation)rep;

   	
     }
}