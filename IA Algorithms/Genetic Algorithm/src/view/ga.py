from numpy import exp, linspace, meshgrid, clip, append, delete, array, argmax
from numpy.random import uniform, choice, rand
from matplotlib import pyplot, use

# Use TkAgg for plotting
use('TkAgg')

# Size of the population
population_size = 10

# Fitness function
def z(x, y):
    """
    Evaluates the fitness of individuals based on their coordinates.
    
    Input:
        x (float): x-coordinate of the individual.
        y (float): y-coordinate of the individual.
    
    Output:
        float: Fitness value calculated based on the input coordinates.
    """
    return exp(-(y + 1) ** 2 - x ** 2) * (x - 1) ** 2 - (exp(-(x + 1) ** 2)) / 3 + exp(-x ** 2 - y ** 2) * (10 * x ** 3 - 10 * x + 10 * y ** 3)

# Elitism: Keep the two best individuals
def elitism(population, fitness):
    """
    Selects and retains the two best individuals from the population.
    
    Input:
        population (ndarray): Current population of individuals.
        fitness (ndarray): Fitness values corresponding to each individual.
    
    Output:
        tuple: A new population containing the best two individuals, 
               the remaining population, and updated fitness values.
    """
    i = argmax(fitness)  # Index of the best individual
    new_population = array([population[i]])  # Start new population with the best individual
    fitness = delete(fitness, i)  # Remove best fitness from the list
    population = delete(population, i, axis=0)  # Remove best individual from the population
    i = argmax(fitness)  # Index of the second best individual
    new_population = append(new_population, [population[i]], axis=0)  # Add the second best individual
    fitness = delete(fitness, i)  # Remove second best fitness from the list
    population = delete(population, i, axis=0)  # Remove second best individual from the population
    return new_population, population, fitness

# Roulette wheel selection method
def roulette_wheel_selection(population, fitness):
    """
    Selects two parents from the population using roulette wheel selection.
    
    Input:
        population (ndarray): Current population of individuals.
        fitness (ndarray): Fitness values corresponding to each individual.
    
    Output:
        tuple: Two selected parent individuals from the population.
    """
    if min(fitness) < 0:
        fitness = [i - min(fitness) + 0.01 for i in fitness]  # Shift fitness values if any are negative
    sum_fitness = sum(fitness)  # Total fitness
    probability = [i / sum_fitness for i in fitness]  # Calculate selection probabilities
    selected0 = choice(population.shape[0], p=probability)  # Select first parent
    selected1 = choice(population.shape[0], p=probability)  # Select second parent
    return population[selected0], population[selected1]

# Average crossover between two parents
def average_crossover(parent0, parent1):
    """
    Performs average crossover between two parents to generate a child.
    
    Input:
        parent0 (ndarray): The first parent individual.
        parent1 (ndarray): The second parent individual.
    
    Output:
        ndarray: The child individual created from the average of both parents.
    """
    averageX = (parent0[0] + parent1[0]) / 2
    averageY = (parent0[1] + parent1[1]) / 2
    return array([[averageX, averageY]])

# Maximum crossover between two parents
def max_crossover(parent0, parent1):
    """
    Performs maximum crossover between two parents to generate a child.
    
    Input:
        parent0 (ndarray): The first parent individual.
        parent1 (ndarray): The second parent individual.
    
    Output:
        ndarray: The child individual created from the maximum of both parents.
    """
    maxX = max(parent0[0], parent1[0])
    maxY = max(parent0[1], parent1[1])
    return array([[maxX, maxY]])

# Mutation of an individual
def mutate(child, mutation_rate):
    """
    Applies mutation to a child individual with a given mutation rate.
    
    Input:
        child (ndarray): The child individual to mutate.
        mutation_rate (float): The probability of mutation occurring.
    
    Output:
        ndarray: The mutated child individual.
    """
    if rand() < mutation_rate:  # Determine if mutation occurs
        child[0][0] += uniform(-0.5, 0.5)  # Mutate x-coordinate
        child[0][1] += uniform(-0.5, 0.5)  # Mutate y-coordinate
        child[0][0] = clip(child[0][0], -4, 4)  # Ensure x is within search space
        child[0][1] = clip(child[0][1], -4, 4)  # Ensure y is within search space
    return child

def ga(num_generations, graph, colors):
    """
    Runs the genetic algorithm for a specified number of generations.
    
    Input:
        num_generations (int): The number of generations to run the algorithm.
        graph (matplotlib.axes.Axes): The graph to plot the results.
        colors (ndarray): An array of colors for plotting each generation.
    
    Output:
        None: The function modifies the population and plots the results.
    """
    # Initial population
    population = uniform(-4, 4, size=(population_size, 2))  # Generate initial random population
    fitness = z(population[:, 0], population[:, 1])  # Calculate fitness for the initial population
    print('Poblacion inicial')
    print(population)
    mutation_rate = 0.1  # Mutation rate
    crosover_size = int((population_size - 2) / 2)  # Number of children to create

    for gen in range(num_generations):
        # Elitism: Keep the best individuals
        new_population, population, fitness = elitism(population, fitness)
        
        # Crossover using average method
        for i in range(crosover_size):
            # Selection
            parent0, parent1 = roulette_wheel_selection(population, fitness)
            child = average_crossover(parent0, parent1)
            # Mutation
            child = mutate(child, mutation_rate)
            new_population = append(new_population, child, axis=0)

        # Crossover using maximum method
        for i in range(crosover_size):
            # Selection
            parent0, parent1 = roulette_wheel_selection(population, fitness)
            child = max_crossover(parent0, parent1)
            # Mutation
            child = mutate(child, mutation_rate)
            new_population = append(new_population, child, axis=0)

        # Update population and calculate fitness
        population = new_population
        fitness = z(population[:, 0], population[:, 1])
        print('Generacion', gen)
        print(population)
        print(fitness)
        graph.scatter(population[:, 0], population[:, 1], fitness, c=colors[gen], cmap=pyplot.cm.viridis, marker='o', alpha=0.66)
        graph.text(4.5, -4.5, 5.5-gen, f'Gen {gen}', color=colors[gen])

    # Find the best individual after all generations
    i = argmax(fitness)
    print('Punto de maxima aptitud')
    print('x: ', population[i][0], ', y: ', population[i][1])
    print('Maxima aptitud')
    print(fitness[i])
    graph.scatter(population[i][0], population[i][1], fitness[i], c='red', marker='x', alpha=1)

def plot(num_generations):
    """
    Plots the 3D surface of the fitness function and runs the genetic algorithm.
    
    Input:
        num_generations (int): The number of generations to run the algorithm.
    
    Output:
        None: Displays the plot with the results of the genetic algorithm.
    """
    if num_generations <= 0:
        return
    ax = pyplot.figure().add_subplot(111, projection='3d')  # Create a 3D axis
    domain = linspace(-6, 6, 100)  # Define the domain for the surface
    x, y = meshgrid(domain, domain)  # Create a meshgrid for x and y
    codomain = z(x, y)  # Calculate the fitness for each (x, y) pair
    colors = pyplot.cm.viridis(linspace(0, 1, num_generations))  # Define colors for plotting
    ax.plot_surface(x, y, codomain, cmap='viridis', alpha=0.34)  # Plot the surface
    ga(num_generations, ax, colors)  # Run the genetic algorithm
    ax.set_xlabel('x')  # Set x-axis label
    ax.set_ylabel('y')  # Set y-axis label
    ax.set_zlabel('z')  # Set z-axis label
    ax.set_title('Algoritmo Genético')  # Set plot title
    pyplot.show()  # Show the plot
