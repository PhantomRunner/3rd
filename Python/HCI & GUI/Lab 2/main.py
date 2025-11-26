import sys
from PyQt5.QtWidgets import QLabel, QLineEdit, QPushButton, QVBoxLayout, QApplication, QWidget
class currency_Converter(QWidget):
    def __init__(self):
        super().__init__()
        self.init_ui()

    def init_ui(self):
        layout = QVBoxLayout()

        self.input_label = QLabel("Amount in euro")
        self.input_field = QLineEdit()
        self.convert_button = QPushButton("Convert")
        self.convert_label = QLabel("Converted Amount:")

        layout.addWidget(self.input_label)
        layout.addWidget(self.input_field)
        layout.addWidget(self.convert_button)
        layout.addWidget(self.convert_label)

        self.convert_button.clicked.connect(self.convert_currency)
        self.setWindowTitle("Currency Converter")
        self.setLayout(layout) # Important: Set the layout of the main window
        self.show()

    def convert_currency(self):
        try:
            amount = float(self.input_field.text())
            rate = 1.17
            result = (amount * rate)
            self.convert_label.setStyleSheet("color: green")  # Reset to default color
            self.convert_label.setText(f"Converted amount: {result:.2f}")
        except ValueError:
            self.convert_label.setStyleSheet("color: red")
            self.convert_label.setText("Error: Enter a valid number")


if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = currency_Converter()
    sys.exit(app.exec_())