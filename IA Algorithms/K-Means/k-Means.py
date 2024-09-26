from tabulate import tabulate
import random
import math

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

def cargarArchivo(dir):
    """
    Loads data from a specified file and stores it in a list of lists.

    :param dir: The directory path to the data file.
    :return: A list of lists containing the data from the file.
    """
    db = []
    f = open(dir, 'r')
    temp = f.readlines()  # Read lines from the file into a list
    f.close()  # Close the file
    for n in temp:  # Split and store data from the file into a list of lists
        aux = n.split(',')
        aux[len(aux) - 1] = aux[len(aux) - 1].replace('\n', '')  # Remove newline characters
        db.append(list(aux))
    return db

def obtenerRangos(db):
    """
    Calculates the ranges of the attributes in the dataset.

    :param db: The dataset as a list of lists.
    :return: A list of ranges for each attribute.
    """
    rangos = []
    min = InicializarLista(len(db[0]) - 1)  # Initialize min list
    max = InicializarLista(len(db[0]) - 1)  # Initialize max list

    # Calculate the ranges for the dataset
    for i in range(len(db)):
        for j in range(1, len(db[0])):
            if db[i][j] != '?':
                if i == 0:
                    min[j - 1] = float(db[i][j])  # Initialize min
                    max[j - 1] = float(db[i][j])  # Initialize max
                else:
                    if min[j - 1] > float(db[i][j]):
                        min[j - 1] = float(db[i][j])  # Update min
                    if max[j - 1] < float(db[i][j]):
                        max[j - 1] = float(db[i][j])  # Update max

    # Prepare ranges for each attribute
    for i in range(len(min)):
        rango = [max[i], min[i]]
        rangos.append(rango)
    return rangos

def imprimirTabla(tabla, headers):
    """
    Prints a formatted table.

    :param tabla: The data to be displayed in the table.
    :param headers: The headers for the table columns.
    """
    print(tabulate(tabla, headers))

def AleatorioFloat(max, min):
    """
    Generates a random floating-point number within a specified range.

    :param max: Maximum value (exclusive).
    :param min: Minimum value (inclusive).
    :return: A random float between min and max.
    """
    aleatorio = random.uniform(min, max)
    return aleatorio

def AleatorioInt(max, min):
    """
    Generates a random integer within a specified range.

    :param max: Maximum value (inclusive).
    :param min: Minimum value (inclusive).
    :return: A random integer between min and max.
    """
    aleatorio = random.randrange(min, max + 1, 1)
    return aleatorio

def Centroides(k, rangos, tiposDato):
    """
    Generates random centroids based on specified ranges and data types.

    :param k: Number of centroids to generate.
    :param rangos: List of ranges for each dimension.
    :param tiposDato: List indicating the data type for each attribute (0 for nominal, 1 for numeric).
    :return: List of generated centroids.
    """
    centroides = []
    centroide = []
    
    for i in range(k):
        for j in range(len(rangos)):
            if tiposDato[j] == 0:
                centroide.append(AleatorioInt(rangos[j][0], rangos[j][1]))  # For nominal data
            else:
                centroide.append(AleatorioFloat(rangos[j][0], rangos[j][1]))  # For numeric data
        centroides.append(list(centroide))  # Append the centroid to the list
        centroide.clear()  # Clear for the next centroid

    return centroides

def kMeans(k, db, headers, tipoDato):
    """
    Implements the K-Means clustering algorithm.

    :param k: Number of clusters (centroids).
    :param db: List of data points to cluster.
    :param headers: Column headers for the dataset.
    :param tipoDato: List indicating the data type for each attribute (0 for nominal, 1 for numeric).
    """
    # List with the ranges of the attributes
    rangos = obtenerRangos(db)
    print(rangos)

    # Generate random centroids
    centroides = Centroides(k, rangos, tipoDato)

    distancias = []
    distanciasCentroides = []
    stop = False
    aux = 0

    while not stop:
        # Calculate distances using HEOM (Heterogeneous Euclidean Overlap Metric)
        for i in range(k):
            distancias.clear()  # Clear distances for the current centroid
            for j in range(len(db)):
                for x in range(1, len(db[0])):
                    if db[j][x] == '?':
                        aux += 1  # Count missing values
                    elif tipoDato[x - 1] == 0:  # Nominal data
                        if float(db[j][x]) == centroides[i][x - 1]:
                            aux += 0  # No distance for equal nominal values
                        else:
                            aux += 1  # Count as a distance for different nominal values
                    else:  # Numeric data
                        aux += ((centroides[i][x - 1] - float(db[j][x])) / (rangos[x - 1][0] - rangos[x - 1][1])) ** 2
                aux = math.sqrt(aux)  # Euclidean distance
                distancias.append(aux)  # Store distance
                aux = 0  # Reset auxiliary variable
            distanciasCentroides.append(list(distancias))  # Append distance list for the current centroid
        
        # Group assignment
        distancias.clear()
        distancias = list(distanciasCentroides[0])  # Start with the first centroid's distances
        grupos = InicializarLista(len(db))
        
        for i in range(1, k):
            for j in range(len(db)):
                if distancias[j] > distanciasCentroides[i][j]:  # Compare distances
                    grupos[j] = i  # Update group assignment
                    distancias[j] = distanciasCentroides[i][j]

        # Recalculate centroids
        centroidesAnteriores = list(centroides)
        distancias = InicializarLista(len(db[0]) - 1)
        
        for i in range(k):
            for j in range(len(db)):
                for x in range(1, len(db[0])):
                    if grupos[j] == i and db[j][x] != '?':  # Only consider valid data points
                        distancias[x - 1] += float(db[j][x])  # Sum values for centroid recalculation
            for y in range(len(distancias)):
                distancias[y] /= len(db)  # Average to find new centroid
            centroides[i] = list(distancias)  # Update centroid
            
        # Stopping condition
        if centroides == centroidesAnteriores:
            stop = True

    # Print results
    tablaGrupos = list(db)  # Create a copy of the original data
    for i in range(len(db)):
        tablaGrupos[i].append(grupos[i])  # Append group assignments
    headers2 = list(headers)
    headers2.append('G')  # Add group header
    imprimirTabla(tablaGrupos, headers2)  # Print the final table

def Main():
    """
    Main function to run K-Means clustering on a sample dataset.
    """
    # Number of centroids
    k = 3
    # Load the data file
    db = cargarArchivo("hepatitis.data")
    # List of attribute headers
    headers = ['Class', 'Age', 'Sex', 'Steroid', 'Antivirals', 'Fatigue', 'Malaise', 'Anorexia',
               'Liver Big', 'Liver Firm', 'Spleen Palpable', 'Spiders', 'Ascites', 'Varices',
               'Bilirubin', 'Alk Phosphate', 'Sgot', 'Albumin', 'Protime', 'Histology']
    # List of data types for each attribute (1 for numeric and 0 for nominal)
    tipoDato = [1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1]
    # Start the K-Means algorithm
    kMeans(k, db, headers, tipoDato)

# Execute the main function
if __name__ == "__main__":
    Main()
