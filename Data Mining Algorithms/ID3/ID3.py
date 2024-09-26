import pandas as pd
import numpy as np
from tree import Tree as Node  # Import Tree class as Node
import tree as tr  # Import tree module for tree visualization
import copy as cp  # Import copy for deep copying

# Classifies an example using a decision tree
# Input: tree (Node), example (pd.Series), indent (str)
# Output: classification (str)
def classify(tree, example, indent):
    if tree.is_leaf:
        print(indent + "Class: " + str(tree.classification))
        return tree.classification
    if example[tree.label] in tree.children:
        print(indent + str(tree.label))
        return classify(tree.children[example[tree.label]], example, indent + "\t")
    else:
        return "No class"

# Calculates entropy of the data based on the class attribute
# Input: data (pd.DataFrame), class_name (str)
# Output: entropy (float)
def entropy(data, class_name):
    entropy = 0
    op_num = data[class_name].value_counts()  # Frequency of class values
    total_data = len(data)
    for op in op_num:
        probability = op / total_data
        entropy -= probability * np.log2(probability)  # Entropy formula
    return int(entropy * 1000) / 1000  # Return rounded value

# Calculates information gain of an attribute
# Input: data (pd.DataFrame), class_name (str), attribute (str)
# Output: gain (float)
def gain(data, class_name, attribute):
    general_entropy = entropy(data, class_name)
    attribute_op = data[attribute].unique()  # Unique values of the attribute
    total_data = len(data)
    attribute_entropy = 0
    for op in attribute_op:
        op_data = data[data[attribute] == op]  # Data subset for each value of the attribute
        op_probability = len(op_data) / total_data
        attribute_entropy += op_probability * entropy(op_data, class_name)
    gain_result = general_entropy - attribute_entropy
    return (gain_result * 1000) / 1000

# Implements the ID3 algorithm to build a decision tree
# Input: data (pd.DataFrame), class_name (str), attributes (list of str), op (str)
# Output: decision tree (Node)
def ID3(data, class_name, attributes, op):
    # Stopping conditions: no attributes or all data belong to one class
    if len(attributes) == 0 or len(data[class_name].unique()) == 1:
        majority_class = data[class_name].mode()[0]  # Majority class
        N = Node(is_leaf=True, classification=majority_class)
    else:
        # Calculate information gain for each attribute
        gains = [gain(data, class_name, attribute) for attribute in attributes]
        max_gain_index = gains.index(max(gains))
        best_attribute = attributes[max_gain_index]
        N = Node(label=best_attribute)  # Create a decision node

        best_attribute_op = data[best_attribute].unique()
        for op in best_attribute_op:
            op_data = data[data[best_attribute] == op]
            op_attributes = [attr for attr in attributes if attr != best_attribute]
            children_node = ID3(op_data, class_name, op_attributes, op)  # Recursively build tree
            N.children[op] = children_node
    return N

# Tests the decision tree on the test dataset
# Input: test_group (pd.DataFrame), tree (Node), class_name (str)
# Output: accuracy (float)
def tree_testing(test_group, tree, class_name):
    ok_tests = 0  # Counter for correct classifications
    total_tests = len(test_group)
    for i, example in test_group.iterrows():
        print("------------------------------------------------------------------\nTest:\n", example)
        print("************************************")
        print("Classification path:")
        predicted_class = classify(tree, example, "")  # Classify the test example
        if predicted_class == example[class_name]:
            ok_tests += 1  # Count correct classifications
    accuracy = ok_tests / total_tests
    return accuracy

# Classifies a set of generated cases using the decision tree
# Input: gen_cases (pd.DataFrame), tree (Node), class_name (str)
# Output: None (prints classification for each case)
def tree_classification(gen_cases, tree, class_name):
    for i, example in gen_cases.iterrows():
        print("------------------------------------------------------------------\nClassification:\n", example)
        print("************************************")
        print("Classification path:")
        predicted_class = classify(tree, example, "")
        print("Predicted class:", predicted_class)

# Generates random cases based on attribute values
# Input: attribute_op (list of arrays), data (pd.DataFrame)
# Output: gen_case (list)
def case_gen(attribute_op, data):
    stop = False
    gen_case = []
    data = data.values
    while not stop:
        for op in attribute_op:
            op_index = np.random.randint(0, len(op))  # Randomly select an option
            gen_case.append(cp.deepcopy(op[op_index]))
        for i in range(len(data)):
            cont = 0
            for j in range(len(data[0]) - 1):
                if data[i][j] == gen_case[j]:
                    cont += 1
            stop = cont != len(data[0]) - 1  # Stop if the generated case is unique
    return gen_case

# Main function to build and test the decision tree
# Input: None
# Output: None (prints decision tree, accuracy, and classification results)
def Main():
    # Load training and test datasets
    training_group = pd.read_csv("entrenamiento.csv")
    test_group = pd.read_csv("prueba.csv")

    class_name = "ca_cervix"  # Name of the class attribute
    attributes = list(training_group.columns)
    attributes.remove(class_name)

    # Print dataset description
    with open('descripcion.txt', 'r') as file:
        contenido = file.read()
    print(contenido)

    # Get unique values for each attribute
    attributes_op = []
    print("Attribute values:")
    for attribute in attributes:
        attribute_op = training_group[attribute].unique()
        print("\t" + attribute + ":" + str(attribute_op))
        attributes_op.append(attribute_op.copy())

    # Build the decision tree using ID3
    tree = ID3(training_group, class_name, attributes, "root")

    # Print the decision tree
    print("---------------------------------------------------------------------------------------------------")
    print("Decision Tree:")
    tr.print_tree(tree)
    print("---------------------------------------------------------------------------------------------------")

    # Test the tree with the test dataset
    print("Test dataset classification:")
    accuracy = tree_testing(test_group, tree, class_name)
    print("---------------------------------------------------------------------------------------------------")
    print(f"Accuracy: {accuracy * 100:.3f}%")

    # Classify generated cases
    print("---------------------------------------------------------------------------------------------------")
    print("Classification of generated cases:")
    pos_com = 1
    for op in attributes_op:
        pos_com *= len(op)
    print("Possible cases:", pos_com)

    file_name = "gen_cases.csv"
    line = ','.join(map(str, attributes))
    with open(file_name, 'w') as file:
        file.write(line)

    for _ in range(5):
        aux = case_gen(attributes_op, training_group)
        line = ','.join(map(str, aux))
        with open(file_name, 'a') as file:
            file.write('\n')
            file.write(line)

    gen_cases = pd.read_csv("gen_cases.csv")
    tree_classification(gen_cases, tree, class_name)

Main()  # Execute the main function
