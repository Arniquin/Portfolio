from customtkinter import CTk, CTkButton, CTkLabel, CTkEntry
from tkinter import IntVar

from src.view.ga import plot  # Import the plotting function from the genetic algorithm module


class Window(CTk):
    def __init__(self):
        """
        Initializes the main window for the genetic algorithm application.
        Sets the window title and appearance mode, and initializes the interface.
        """
        super().__init__()
        self.title('Algoritmo Genético')  # Set the title of the window
        # self._set_appearance_mode('dark')  # Uncomment to set dark mode
        # self._set_appearance_mode('light')  # Uncomment to set light mode
        self._set_appearance_mode('system')  # Set appearance mode based on the system settings
        self.__keyboard = IntVar(value=1)  # Variable to hold the number of generations input
        self.__init()  # Initialize the GUI components
        self.resizable(False, False)  # Make the window non-resizable

    def __init(self):
        """
        Initializes the user interface components of the window.
        Creates labels, an entry field, and a button to execute the plot function.
        """
        CTkLabel(self, text='Algoritmo Genético', font=('arial bold', 24)).pack(padx=50, pady=25)  # Title label
        CTkLabel(self, text='Numero de Generaciones', font=('arial bold', 14)).pack()  # Label for input
        CTkEntry(self, textvariable=self.__keyboard).pack()  # Entry field for number of generations
        CTkButton(
            self,
            text='Graficar',  # Button label
            font=('arial bold', 14),
            command=lambda: plot(self.__keyboard.get())  # Execute the plot function with user input
        ).pack(pady=15)  # Add button to the window
