from PyQt6.QtWidgets import QApplication, QWidget, QMainWindow, QFileDialog, QDockWidget, QPushButton, QVBoxLayout, \
    QLabel, QMessageBox, QInputDialog
from PyQt6.QtGui import QIcon, QPainter, QPen, QAction, QPixmap, QColor
from PyQt6.QtCore import Qt, QPoint
import sys
import csv
import random

class PictionaryGame(QMainWindow):  # documentation https://doc.qt.io/qt-6/qwidget.html
    '''
    Painting Application class
    '''

    def __init__(self):
        super().__init__()

        # set window title
        self.setWindowTitle("Pictionary Game")

        # set the windows dimensions
        self.setGeometry(400, 400, 800, 600)

        # image settings (default)
        self.image = QPixmap("./icons/canvas.png")  # documentation: https://doc.qt.io/qt-6/qpixmap.html
        self.image.fill(Qt.GlobalColor.white)  # documentation: https://doc.qt.io/qt-6/qpixmap.html#fill
        mainWidget = QWidget()
        mainWidget.setMaximumWidth(300)

        # draw settings (default)
        self.drawing = False
        self.brushSize = 3
        self.brushColor = Qt.GlobalColor.black  # documentation: https://doc.qt.io/qt-6/qt.html#GlobalColor-enum

        # reference to last point recorded by mouse
        self.lastPoint = QPoint()  # documentation: https://doc.qt.io/qt-6/qpoint.html

        # --- MENU BAR SETUP ---

        '''
           Create a menu bar on top of the program and add following section.
           1. File menu with 4 actions - Save, Open, Clear and exit
           2. Brush size with 4 options - 3px, 5px, 7px, 9px
           3. Brush color - Red,Black,Yellow,Green
           4. Game - 1 option to start new game
           5. Help - 1 option, about. Description of the game 
        '''

        mainMenu = self.menuBar()  # create a menu bar
        mainMenu.setNativeMenuBar(False)

        fileMenu = mainMenu.addMenu(" File")  # add the file menu to the menu bar, the space is required as "File" is reserved in Mac

        brushSizeMenu = mainMenu.addMenu(" Brush Size")  # add the "Brush Size" menu to the menu bar

        brushColorMenu = mainMenu.addMenu(" Brush Colour")  # add the "Brush Colour" menu to the menu bar
        self.brushColorName = "Black" # variable to store color of the brush, for displaying it in the status bar

        gameMenu = mainMenu.addMenu("Game")
        helpMenu = mainMenu.addMenu("Help")

        # --- file menu ---
        '''
            Initialize 4 actions. Save, Open, Clear and Exit.
            Set a png icon to each of the actions.
            Set a hot key for fast call
        '''
        #  save action
        saveAction = QAction(QIcon("./icons/save.png"), "Save",self)  # create a save action with a png as an icon, documentation: https://doc.qt.io/qt-6/qaction.html
        saveAction.setShortcut("Ctrl+S")  # connect this save action to a keyboard shortcut, documentation: https://doc.qt.io/qt-6/qaction.html#shortcut-prop
        fileMenu.addAction(saveAction)  # add the save action to the file menu, documentation: https://doc.qt.io/qt-6/qwidget.html#addAction
        saveAction.triggered.connect(self.save)  # when the menu option is selected or the shortcut is used the save slot is triggered, documentation: https://doc.qt.io/qt-6/qaction.html#triggered

        #  open action
        openAction = QAction(QIcon("./icons/open.png"), "Open", self)
        openAction.setShortcut("Ctrl+O")
        openAction.triggered.connect(self.open)
        fileMenu.addAction(openAction)

        #  clear action
        clearAction = QAction(QIcon("./icons/clear.png"), "Clear", self)
        clearAction.setShortcut("Ctrl+C")
        fileMenu.addAction(clearAction)
        clearAction.triggered.connect(self.clear)

        #  exit action
        exitAction = QAction(QIcon("./icons/exit.png"), "Exit", self)
        exitAction.setShortcut("Ctrl+Q")
        exitAction.triggered.connect(self.exitApp)
        fileMenu.addAction(exitAction)

        # --- game menu ---
        '''
            Initialize 1 action to start a new game
            Set a hot key for fast call
            call a event-driven function
        '''
        #   Start-Game action
        startGameAction = QAction("Start Game", self)
        startGameAction.setShortcut("Ctrl+N")
        startGameAction.triggered.connect(self.startGame)
        gameMenu.addAction(startGameAction)

        # --- help menu ---
        '''
            Initialize 1 action to start a new game
            Set a hot key for fast call
            Set a png icon
            call a event-driven function
        '''
        #  about action
        aboutAction = QAction(QIcon("./icons/about.png"), "About", self)
        aboutAction.setShortcut("F1")
        aboutAction.triggered.connect(self.showAbout)
        helpMenu.addAction(aboutAction)

        # --- Brush-thickness menu ---
        '''
            Initialize 4 different options with different brush sizes
            Set a hot key for fast call
            Set a png icon
            Call a event-driven function
        '''
        # brush thickness
        threepxAction = QAction(QIcon("./icons/threepx.png"), "3px", self)
        threepxAction.setShortcut("Ctrl+3")
        brushSizeMenu.addAction(threepxAction)  # connect the action to the function below
        threepxAction.triggered.connect(self.threepx)

        fivepxAction = QAction(QIcon("./icons/fivepx.png"), "5px", self)
        fivepxAction.setShortcut("Ctrl+5")
        brushSizeMenu.addAction(fivepxAction)
        fivepxAction.triggered.connect(self.fivepx)

        sevenpxAction = QAction(QIcon("./icons/sevenpx.png"), "7px", self)
        sevenpxAction.setShortcut("Ctrl+7")
        brushSizeMenu.addAction(sevenpxAction)
        sevenpxAction.triggered.connect(self.sevenpx)

        ninepxAction = QAction(QIcon("./icons/ninepx.png"), "9px", self)
        ninepxAction.setShortcut("Ctrl+9")
        brushSizeMenu.addAction(ninepxAction)
        ninepxAction.triggered.connect(self.ninepx)

        # --- Brush-Colors menu ---
        """
            Initialize 4 different options with different brush sizes
            Set a hot key for fast call
            Set a png icon
            call a event-driven function
        """
        # brush colors
        blackAction = QAction(QIcon("./icons/black.png"), "Black", self)
        blackAction.setShortcut("Ctrl+B")
        brushColorMenu.addAction(blackAction)
        blackAction.triggered.connect(self.black)

        redAction = QAction(QIcon("./icons/red.png"), "Red", self)
        redAction.setShortcut("Ctrl+R")
        brushColorMenu.addAction(redAction)
        redAction.triggered.connect(self.red)

        greenAction = QAction(QIcon("./icons/green.png"), "Green", self)
        greenAction.setShortcut("Ctrl+G")
        brushColorMenu.addAction(greenAction)
        greenAction.triggered.connect(self.green)

        yellowAction = QAction(QIcon("./icons/yellow.png"), "Yellow", self)
        yellowAction.setShortcut("Ctrl+Y")
        brushColorMenu.addAction(yellowAction)
        yellowAction.triggered.connect(self.yellow)

        # --- DOCK UI ---
        # Create a dockable widget (i.e. a widget that can be undocked and floated)
        self.dockInfo = QDockWidget("") # reference https://doc.qt.io/qt-6/qdockwidget.html

        # Disable floating and closing features, keeping the dock fixed
        self.dockInfo.setFeatures(QDockWidget.DockWidgetFeature.NoDockWidgetFeatures)

        # Add the dock to the main window on the left side
        self.addDockWidget(Qt.DockWidgetArea.LeftDockWidgetArea, self.dockInfo)

        # --- Layout for Dock Contents ---
        playerInfo = QWidget()  # container widget for the dock
        dock = QVBoxLayout() # vertical layout for arranging widgets in a column
        dock.setContentsMargins(12, 12, 12, 12) # padding inside the dock
        dock.setSpacing(10) # spacing between elements
        playerInfo.setLayout(dock) # apply layout to the container
        playerInfo.setMaximumWidth(220) # limit the width of the dock

        # Title for the dock
        title = QLabel("PICTIONARY") # display text
        title.setStyleSheet("font-size: 22px; font-weight: bold; padding: 8px;") # set styles
        title.setAlignment(Qt.AlignmentFlag.AlignCenter) # center the text
        dock.addWidget(title) # add title to the layout

        # Start-Game button inside the dock
        startBtn = QPushButton("🎮  Start Game")  # Create button
        startBtn.clicked.connect(self.startGame)  # link button to startGame method
        startBtn.setStyleSheet("font-size: 14px; padding: 10px;") # set styles
        dock.addWidget(startBtn)  # add the button to the layout

        dock.addSpacing(10)   # add empty space for separation

        # Label to show current turn
        self.turnLabel = QLabel("Current Turn: -")  # Display the player's turn
        self.turnLabel.setStyleSheet("font-size: 16px; font-weight: bold; color: #333; padding: 5px;")
        dock.addWidget(self.turnLabel)  # add label to dock

        dock.addSpacing(10)  # add separation space

        # Score section
        scoreTitle = QLabel("Scores")  # label for the scores section
        scoreTitle.setStyleSheet("font-size: 18px; font-weight: bold;")
        dock.addWidget(scoreTitle)  # add to layout

        # Labels for Player scores
        self.player1Label = QLabel("Player 1: 0")
        self.player2Label = QLabel("Player 2: 0")
        self.player1Label.setStyleSheet("font-size: 15px; padding-left: 10px;")
        self.player2Label.setStyleSheet("font-size: 15px; padding-left: 10px;")
        dock.addWidget(self.player1Label)  # add player 1 label
        dock.addWidget(self.player2Label)  # add player 2 label

        dock.addSpacing(15) # space between scores and action buttons

        # Action buttons
        # Button to switch role from drawer to guesser
        passBtn = QPushButton("➡  Switch to Guesser")
        passBtn.clicked.connect(self.passTurn)  # connect to passTurn method
        dock.addWidget(passBtn)

        # Button to skip the current word
        skipBtn = QPushButton("⏭  Skip Word")
        skipBtn.clicked.connect(self.skipTurn)  # connect to skipTurn method
        dock.addWidget(skipBtn)

        # Button to reveal the current word
        revealBtn = QPushButton("👁  Reveal Word")
        revealBtn.clicked.connect(self.revealWord)  # connect to revealWord method
        dock.addWidget(revealBtn)

        dock.addStretch(1) # push all widgets to top, remaining space stays empty

        # Set the container widget with layout as dock content
        self.dockInfo.setWidget(playerInfo)

        # --- GAME LOGIC INIT ---
        # Load word list based on difficulty level
        self.getList("easy")  # read "easymode.txt" for words
        self.currentWord = self.getWord()  # pick a random word from the list

        # Initialize turn and scores
        # 1 = Player 1 drawing, 2 = Player 2 guessing
        self.turn = 1
        self.playerScores = [0, 0]  # initial scores

        self.updateTurnDisplay()  # update GUI to show current turn and scores
        self.clear()  # clear the canvas at the start of the game

        # --- STATUS BAR ---
        self.statusBar().showMessage("Ready")  # set initial status bar message

        # --- STYLESHEET ---
        # Apply general styling for the application, menus, buttons, and labels
        self.setStyleSheet("""
            QMainWindow {
                background: #f7f7ff;
            }

            QPushButton {
                background: #c49991;  
                color: white;          
                border: 1px solid #004d40;
                padding: 8px;
                border-radius: 6px;
                font-size: 14px;
            }
            QPushButton:hover {
                background: #C4887E;
            }
            QPushButton:pressed {
                background: #E3978A;
            }
            QLabel {
                font-size: 14px;
                padding: 3px;
            }
            QMenuBar {
                background: #fafafa;
                border-bottom: 1px solid #c0c0c0;
            }
            QMenu {
                background: #ffffff;
            }
        """)

    # event handlers
    def updateStatusBar(self):
        """
            Update the status bar to display game status:
            - The brush size
            - Current brush color
            - Current player's turn. Drawer or Guesser

            Method is called every time the turn changes or when any related game setting changes.
        """
        if self.turn == 1:
            # If it's player 1's turn, they're the "Drawer"
            turn_role = "Drawer"
        else:
            # If it's player 2's turn, they're the "Guesser"
            turn_role = "Guesser"

        # display status bar
        self.statusBar().showMessage(
            f"Brush: {self.brushSize}px  |  Colour: {self.brushColorName}  |  Turn: {turn_role}"
        )

    def showAbout(self):
        """
            Show a message box displaying details and rules of the game
        """

        QMessageBox.information(
            self,
            "About Pictionary Game",
            "Pictionary Game\n"
            "Created with PyQt6\n\n"
            "Draw and guess words with a friend!\n"
            "Controls: Use the mouse to draw, Pass to guess, Skip to change word."
        )

    def startGame(self):
        """
            Start a new game. Resetting player scores, selecting a new word for the current round,
            and initializing the new,clear game environment.

            This method performs the following:
            - Resets the player scores to 0
            - Sets the first player as the drawer (player 1)
            - Picks a new word for the drawer to draw
            - Clears the drawing canvas
            - Shows the word to the drawer
        """

        # Reset player scores at the beginning of the game
        self.playerScores = [0, 0]  # Scores for Player 1 and Player 2

        # Update the turn display (which player is currently playing)
        self.updateTurnDisplay()

        # Reset the turn to Player 1 (Drawer)
        self.turn = 1

        # Pick a new word for the drawer to draw
        self.currentWord = self.getWord()

        # Clear the drawing canvas
        self.clear()

        # Reveal the word to the drawer
        self.revealToDrawer()

    def revealWord(self):
        """
           Show the current word in a message box.
           Used for revealing the word or providing feedback during the game.
        """
        QMessageBox.information(self, "Word", f"The word is: {self.currentWord}")

    def passTurn(self):
        """
            Pass the current turn to the other player (from Drawer to Guesser),
            and prompt the guesser to enter their guess.

            This method works in following way:
            - Determines the current drawer and guesser based on the current turn
            - Switches turns (pass the turn to the guesser)
            - Prompts the guesser for their guess via a dialog box
            - Checks if the guess is correct:
                - If correct, update scores and show a success message
                - If incorrect, show a failure message
            - After processing the guess, move to the next round and:
                - Select a new word
                - Clear the canvas
                - Return the turn to the drawer
                - Reveal the new word to the drawer
        """

        # Identify roles
        drawer = self.turn  # drawer is always current turn

        if drawer == 1:
            guesser = 2  # If drawer is player 1, then player 2 is the guesser
        else:
            guesser = 1  # If drawer is player 2, then player 1 is the guesser

        # Update turn label BEFORE showing popup
        self.turn = guesser
        self.updateTurnDisplay()

        # Prompt the guesser to enter their guess

        # reference https://doc.qt.io/qt-6/qinputdialog.html
        # reference https://www.geeksforgeeks.org/python/pyqt5-input-dialog-python/
        guess, ok = QInputDialog.getText(
            self,
            f"Player {guesser} – Make a Guess",
            "Enter your guess:"
        )

        if not ok:  # If user cancels or enters nothing, just return
            # Bug fix. In case when turn go back to drawer - update the status bar
            self.turn = drawer
            self.updateTurnDisplay()
            return

        # Check if the guess is correct (ignoring case and extra spaces)
        correct_guess = guess.strip().lower() == self.currentWord.lower()

        if correct_guess:
            # Inform the players that the guess is correct

            # reference https://doc.qt.io/qt-6/qmessagebox.html
            # reference https://pythonguides.com/qmessagebox-pyqt6/
            QMessageBox.information(
                self,
                "Correct!",
                f"Player {guesser} guessed correctly!\n\n"
                f"Points awarded: +1 for Player {guesser}, +2 for Player {drawer}"
            )

            # Update the scores: guesser gets 1 point, drawer gets 2 points
            self.playerScores[guesser - 1] += 1
            self.playerScores[drawer - 1] += 2
        else:
            # If the guess is incorrect, show the correct word and start new game
            QMessageBox.information(
                self,
                "Incorrect",
                f"Wrong guess! The word was: {self.currentWord}"
            )

            # Pick a new word for the next round
            self.currentWord = self.getWord()

            # Clear canvas for next round
            self.clear()

            # Switch the turn back to the drawer
            self.turn = drawer
            self.updateTurnDisplay()

            # Reveal the new word to the drawer
            self.revealToDrawer()

    def skipTurn(self):
        """
            Skip current round, revealing the word to both players without guessing.

            Steps involved:
            - Show a message indicating the current word was skipped
            - Pick a new word for the next round
            - Clear the canvas
            - Update the turn display and reveal the new word to the drawer
        """
        # reference https://doc.qt.io/qt-6/qmessagebox.html
        # reference https://pythonguides.com/qmessagebox-pyqt6/
        QMessageBox.information(
            self,
            "Skipped", "The word was: " + self.currentWord)

        # Pick a new word for the next round
        self.currentWord = self.getWord()

        # Clear canvas for next round
        self.clear()

        # Update the turn display
        self.updateTurnDisplay()

        # Reveal the new word to the drawer
        self.revealToDrawer()

    def updateTurnDisplay(self):
        """
            Update the game labels to keep current player's turn and their scores up to date
            Method performs following actions:
            - Updates turn label to show who is currently playing Drawer or Guesser
            - Updates score labels for both players
            - Updates status bar to show the current brush size, color, and role Drawer or Guesser
        """

        # Pick the current role (Drawer or Guesser)
        if self.turn == 1:
            role = "Drawer"
        else:
            role = "Guesser"

        # Update the turn label
        self.turnLabel.setText(f"Current Turn: {role}")

        # Update the player scores
        self.player1Label.setText(f"Player 1: {self.playerScores[0]}")
        self.player2Label.setText(f"Player 2: {self.playerScores[1]}")

        # Update the status bar
        self.updateStatusBar()

    def revealToDrawer(self):
        """
          Reveal current selected word to the drawer by displaying it in an information dialog.
          The drawer is picked based on the current turn (1 = Player 1, 2 = Player 2).
        """
        #reference https://pythonguides.com/qmessagebox-pyqt6/
        drawer = self.turn
        QMessageBox.information(
            self,
            f"Player {drawer} – Your Word",
            f"Draw this word:\n\n{self.currentWord}"
        )

    def mousePressEvent(self, event):  # when the mouse is pressed, documentation: https://doc.qt.io/qt-6/qwidget.html#mousePressEvent

        if event.pos().x() < self.dockInfo.width():
            return

        if event.button() == Qt.MouseButton.LeftButton:  # if the pressed button is the left button
            self.drawing = True  # enter drawing mode
            self.lastPoint = event.pos()  # save the location of the mouse press as the lastPoint
            print(self.lastPoint)  # print the lastPoint for debugging purposes

    def mouseMoveEvent(self, event):  # when the mouse is moved, documenation: documentation: https://doc.qt.io/qt-6/qwidget.html#mouseMoveEvent
        if event.pos().x() < self.dockInfo.width():
            return

        if self.drawing:
            painter = QPainter(self.image)  # object which allows drawing to take place on an image
            # allows the selection of brush colour, brish size, line type, cap type, join type. Images available here http://doc.qt.io/qt-6/qpen.html
            painter.setPen(QPen(self.brushColor, self.brushSize, Qt.PenStyle.SolidLine, Qt.PenCapStyle.RoundCap,
                                Qt.PenJoinStyle.RoundJoin))
            painter.drawLine(self.lastPoint,
                             event.pos())  # draw a line from the point of the orginal press to the point to where the mouse was dragged to
            self.lastPoint = event.pos()  # set the last point to refer to the point we have just moved to, this helps when drawing the next line segment
            self.update()  # call the update method of the widget which calls the paintEvent of this class

    def mouseReleaseEvent(self, event):  # when the mouse is released, documentation: https://doc.qt.io/qt-6/qwidget.html#mouseReleaseEvent
        if event.pos().x() < self.dockInfo.width():
            return

        if event.button() == Qt.MouseButton.LeftButton:  # if the released button is the left button, documentation: https://doc.qt.io/qt-6/qt.html#MouseButton-enum ,
            self.drawing = False  # exit drawing mode

    # paint events
    def paintEvent(self, event):
        # you should only create and use the QPainter object in this method, it should be a local variable
        canvasPainter = QPainter(self)  # create a new QPainter object, documentation: https://doc.qt.io/qt-6/qpainter.html
        canvasPainter.drawPixmap(QPoint(),self.image)  # draw the image , documentation: https://doc.qt.io/qt-6/qpainter.html#drawImage-1

    # resize event - this function is called
    def resizeEvent(self, event):
        self.image = self.image.scaled(self.width(), self.height())

    # slots
    def save(self):
        filePath, _ = QFileDialog.getSaveFileName(
            self, "Save Image", "",
            "PNG (*.png);;JPG (*.jpg *.jpeg);;All Files (*.*)"
        )
        if filePath == "":
            return

        self.image.save(filePath)

    def open(self):
        filePath, _ = QFileDialog.getOpenFileName(
            self, "Open Image", "",
            "PNG (*.png);;JPG (*.jpg *.jpeg);;All Files (*.*)"
        )
        if not filePath:
            return

        loadedImage = QPixmap(filePath)
        # Resize to match canvas
        self.image = loadedImage.scaled(self.size(), Qt.AspectRatioMode.KeepAspectRatio,
                                        Qt.TransformationMode.SmoothTransformation)
        self.update()

    def clear(self):
        self.image.fill(
            Qt.GlobalColor.white)  # fill the image with white, documentation: https://doc.qt.io/qt-6/qimage.html#fill-2
        self.update()  # call the update method of the widget which calls the paintEvent of this class

    def exitApp(self, event):
        """
        Display a confirmation dialog asking the user if they want to exit the application.
        If the user clicks 'Yes', the application window will close. If they click 'No', nothing happens.
        If 'Cancel' is clicked, nothing happens as well.
        """
        # Create a detailed exit confirmation message box
        # reference https://doc.qt.io/qt-6/qmessagebox.html
        # reference https://coderscratchpad.com/pyqt6-confirming-program-exit/
        message_box = QMessageBox(self)  # The parent widget is the main window
        message_box.setText('Are you sure you want to exit?')
        message_box.setWindowTitle('Exit Confirmation')
        message_box.setStandardButtons(QMessageBox.StandardButton.Yes |
                                   QMessageBox.StandardButton.No)  # Yes, No

        message_box.setDefaultButton(QMessageBox.StandardButton.No)  # Set the default button to 'No'

        # Execute the message box
        reply = message_box.exec()

        if reply == QMessageBox.StandardButton.Yes:
            self.close()

    def threepx(self):  # the brush size is set to 3
        self.brushSize = 3

    def fivepx(self):
        self.brushSize = 5
        self.updateStatusBar()

    def sevenpx(self):
        self.brushSize = 7
        self.updateStatusBar()

    def ninepx(self):
        self.brushSize = 9
        self.updateStatusBar()

    def black(self):  # the brush color is set to black
        self.brushColor = Qt.GlobalColor.black
        self.brushColorName = "Black"
        self.updateStatusBar()

    def red(self):
        self.brushColor = Qt.GlobalColor.red
        self.brushColorName = "Red"
        self.updateStatusBar()

    def green(self):
        self.brushColor = Qt.GlobalColor.green
        self.brushColorName = "Green"
        self.updateStatusBar()

    def yellow(self):
        self.brushColor = Qt.GlobalColor.yellow
        self.brushColorName = "Yellow"
        self.updateStatusBar()

    # Get a random word from the list read from file
    def getWord(self):
        randomWord = random.choice(self.wordList)
        print(randomWord)
        return randomWord

    # read word list from file
    def getList(self, mode):
        with open(mode + 'mode.txt') as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=',')
            line_count = 0
            for row in csv_reader:
                # print(row)
                self.wordList = row
                line_count += 1
            # print(f'Processed {line_count} lines.')

    # open a file
    def open(self):
        '''
        This is an additional function which is not part of the tutorial. It will allow you to:
         - open a file dialog box,
         - filter the list of files according to file extension
         - set the QImage of your application (self.image) to a scaled version of the file)
         - update the widget
        '''
        filePath, _ = QFileDialog.getOpenFileName(self, "Open Image", "",
                                                  "PNG(*.png);;JPG(*.jpg *.jpeg);;All Files (*.*)")
        if filePath == "":  # if not file is selected exit
            return
        with open(filePath, 'rb') as f:  # open the file in binary mode for reading
            content = f.read()  # read the file
        self.image.loadFromData(content)  # load the data into the file
        width = self.width()  # get the width of the current QImage in your application
        height = self.height()  # get the height of the current QImage in your application
        self.image = self.image.scaled(width, height)  # scale the image from file and put it in your QImage
        self.update()  # call the update method of the widget which calls the paintEvent of this class

if __name__ == '__main__':
    app = QApplication(sys.argv)

    # mac only - the OS sets icons to 'hidden' by default otherwise:
    app.setAttribute(Qt.ApplicationAttribute.AA_DontShowIconsInMenus, False)

    window = PictionaryGame()
    window.show()
    sys.exit(app.exec())
