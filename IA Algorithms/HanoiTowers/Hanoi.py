import copy as cp

class Node():
    def __init__(self, state, parent=None, position=None):
        """
        Initializes a node in the A* search algorithm.
        
        :param state: Current state of the Towers of Hanoi.
        :param parent: Parent node (previous state).
        :param position: Position of the node in the state.
        """
        self.parent = parent
        self.position = position
        self.state = state
        self.g = 0  # Cost from start to this node
        self.h = 0  # Heuristic cost estimate to goal
        self.f = 0  # Total cost (g + h)

    def __eq__(self, other):
        """Checks equality of nodes based on their position."""
        return self.position == other.position

def heuristic(current_state, final_state, n):
    """
    Heuristic function to estimate the number of disks missing from the final state.

    :param current_state: Current state of the Towers of Hanoi.
    :param final_state: The desired final state.
    :param n: Total number of disks.
    :return: Estimated number of disks to reach the final state.
    """
    # Find the index of the non-empty final tower
    for i in range(3):
        if len(final_state[i]) != 0:
            index = i
    return n - len(current_state[index])

def heuristic_top_disks(current_state, final_state, n):
    """
    Heuristic function to count disks on top of the final disks.

    :param current_state: Current state of the Towers of Hanoi.
    :param final_state: The desired final state.
    :param n: Total number of disks.
    :return: Estimated number of disks on top of the target disks.
    """
    total_top_disks = 0
    for i in range(3):
        if len(final_state[i]) != 0:
            index = i  # Tower with disks in final state

    for i in range(3):
        if index != i:
            for j in range(len(current_state[i])):
                for x in range(j):
                    total_top_disks += 1  # Count disks above the final disks
    return total_top_disks

def generate_children(current_node):
    """
    Generates child nodes for a given state by moving disks between towers.

    :param current_node: Current node whose children are to be generated.
    :return: List of child nodes.
    """
    current_state = cp.deepcopy(current_node.state)  # Copy current state
    children = []
    
    for i in range(3):
        if len(current_state[i]) != 0:  # If tower is not empty
            for j in range(3):
                change = False
                if current_state[i] != current_state[j]:  # Ensure different towers
                    temp_state = cp.deepcopy(current_node.state)  # Create a temporary state
                    
                    if len(temp_state[j]) == 0:  # If the destination tower is empty
                        temp_state[j].append(temp_state[i].pop())  # Move disk
                        change = True
                    elif temp_state[j][-1] > temp_state[i][-1]:  # Valid move
                        temp_state[j].append(temp_state[i].pop())
                        change = True
                
                # If a change occurred and the new node is not already a child
                if change and Node(temp_state) not in children:
                    children.append(Node(temp_state, current_node, temp_state))
    return children

def a_star(towers, final_state, n):
    """
    Implements the A* search algorithm to find the path from the initial state to the final state.

    :param towers: Initial state of the towers.
    :param final_state: Desired final state of the towers.
    :param n: Total number of disks.
    :return: List of states from start to goal.
    """
    children = []
    to_explore_nodes = []  # Nodes to explore
    explored_nodes = []  # Nodes already explored
    initial_node = Node(towers)  # Create initial node
    final_node = Node(final_state)  # Create final node
    to_explore_nodes.append(initial_node)  # Add initial node to the list

    while to_explore_nodes:
        current_node = to_explore_nodes[0]  # Assume the first node is the best
        current_index = 0

        # Find the node with the lowest f value
        for index, item in enumerate(to_explore_nodes):
            if item.f < current_node.f:
                current_node = item
                current_index = index

        to_explore_nodes.pop(current_index)  # Remove the current node from the list
        explored_nodes.append(current_node)  # Mark it as explored

        # If the current node is the final state, build the path
        if current_node.state == final_node.state:
            path = []
            current = current_node
            while current is not None:
                path.append(current.state)
                current = current.parent  # Move up to the parent
            return path[::-1]  # Return the path in reverse order

        children_list = generate_children(current_node)  # Generate children nodes
        for child in children_list:
            children.append(child)

        for child in children:
            if child in explored_nodes:
                continue  # Skip if already explored

            child.g = current_node.g + 1  # Update g value (cost)
            child.h = heuristic_top_disks(current_node.state, final_state, n)  # Update h value
            child.f = child.g + child.h  # Update f value

            if child in to_explore_nodes and child.g > current_node.g:
                continue  # Skip if not better than previous

            to_explore_nodes.append(child)  # Add child to the list of nodes to explore

def tower_initialization(tower_index, n):
    """
    Initializes the towers with disks.

    :param tower_index: The index of the tower to start with (0, 1, or 2).
    :param n: Total number of disks.
    :return: A list representing the towers and their disks.
    """
    towers = []  # List to hold the towers
    for i in range(3):
        towers.append([])  # Initialize each tower as an empty list

    disks = n  # Start with n disks
    for _ in range(n):
        towers[tower_index].append(disks)  # Add disks to the specified tower
        disks -= 1  # Decrease the disk size for the next disk

    return towers

def main():
    """
    Main function to run the Towers of Hanoi algorithm and display results.
    """
    n = 6  # Number of disks in the Towers of Hanoi
    towers = tower_initialization(0, n)  # Generate the initial state
    final_estate_1 = tower_initialization(1, n)  # Generate the first final state
    final_estate_2 = tower_initialization(2, n)  # Generate the second final state
    
    # Print initial and final states
    print("Estado Inicial:")
    print(towers)
    print("\nEstado Final 1:")
    print(final_estate_1)
    print("\nEstado Final 2:")
    print(final_estate_2)
    
    print("Camino:-------------------------------------------")
    path = a_star(towers, final_estate_2, n)  # Find the path to the second final state
    for i in path:
        print(i)  # Print each state in the path

# Execute the main function
if __name__ == "__main__":
    main()
