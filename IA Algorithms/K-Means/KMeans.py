import random
import math

def Aleatorio(max, min):
    """
    Generates a random integer within a specified range.

    :param max: Maximum value (exclusive).
    :param min: Minimum value (inclusive).
    :return: A random integer between min and max.
    """
    aleatorio = random.randrange(min, max, 1)
    return aleatorio

def Centroides(k, rangos):
    """
    Generates random centroids based on specified ranges.

    :param k: Number of centroids to generate.
    :param rangos: List of ranges for each dimension.
    :return: List of generated centroids.
    """
    centroides = []
    centroide = []
    
    for i in range(k):
        for j in range(len(rangos)):
            centroide.append(Aleatorio(rangos[j][0], rangos[j][1]))  # Generate random centroid
        centroides.append(list(centroide))  # Append the centroid to the list
        centroide.clear()  # Clear for the next centroid

    return centroides

def InicializarLista(k):
    """
    Initializes a list of zeros with a specified length.

    :param k: Length of the list.
    :return: A list of zeros of length k.
    """
    lista = []
    for i in range(k):
        lista.append(0)
    return lista

def k_Means(k, database):
    """
    Implements the K-Means clustering algorithm.

    :param k: Number of clusters (centroids).
    :param database: List of data points to cluster.
    """
    rangos = []
    min = InicializarLista(len(database[0]) - 1)  # Initialize min list
    max = InicializarLista(len(database[0]) - 1)  # Initialize max list

    # Calculate ranges for the dataset
    for i in range(len(database)):
        for j in range(1, len(database[0])):
            if i == 0:
                min[j - 1] = database[i][j]  # Initialize min
                max[j - 1] = database[i][j]  # Initialize max
            else:
                if min[j - 1] > database[i][j]:
                    min[j - 1] = database[i][j]  # Update min
                if max[j - 1] < database[i][j]:
                    max[j - 1] = database[i][j]  # Update max

    # Prepare ranges for centroid generation
    for i in range(len(min)):
        rango = [max[i], min[i]]
        rangos.append(rango)

    print("-------------------------\nRangos:")
    print(rangos)

    # Obtain centroids based on ranges
    centroides = Centroides(k, rangos)
    print("-------------------------\nCentroides:")
    print(centroides)

    # K-Means algorithm execution
    grupos = []
    distancias = []
    distanciasCentroides = []
    stop = False
    
    while not stop:
        # Calculate distances from each data point to each centroid
        for x in range(k):
            for i in range(len(database)):
                dis = 0
                for j in range(1, len(database[0])):
                    dis += (centroides[x][j - 1] - database[i][j]) ** 2  # Squared distance
                dis = math.sqrt(dis)  # Euclidean distance
                distancias.append(dis)  # Store distance
            distanciasCentroides.append(list(distancias))  # Append distance list for the current centroid
            distancias.clear()  # Clear for the next centroid distances

        print("-------------------------\nDistancias de centroides:")
        print(distanciasCentroides)

        # Assign groups based on the closest centroid
        distancias.clear()
        grupos = InicializarLista(len(distanciasCentroides[0]))
        
        for i in range(len(distanciasCentroides[0])):
            distancias = list(distanciasCentroides[0])  # Start with the first centroid's distances
            for j in range(1, k):
                if distanciasCentroides[j][i] < distancias[i]:  # Compare distances
                    distancias[i] = distanciasCentroides[j][i]
                    grupos[i] = j  # Update group assignment
        
        print("-------------------------\nGrupos:")
        print(grupos)

        # Recalculate centroids (this part is not yet implemented)
        
        stop = True  # Stop condition (to be updated for actual logic)

def Main():
    """
    Main function to run K-Means clustering on a sample dataset.
    """
    database = []
    database.append([0, 23, 9, 6, 1])
    database.append([1, 12, 7, 4, 2])
    database.append([2, 43, 5, 3, 2])
    database.append([3, 54, 3, 1, 1])
    database.append([4, 45, 8, 3, 2])

    print("Base de datos:")
    print(database)
    k_Means(2, database)  # Run K-Means with 2 clusters

# Execute the main function
if __name__ == "__main__":
    Main()
