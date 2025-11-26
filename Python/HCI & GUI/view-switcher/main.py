from PyQt5.QtWidgets import QApplication, QWidget, QVBoxLayout, QPushButton, QListWidget, QTableWidget, QTableWidgetItem
import sys
import csv


class ViewSwitcher(QWidget):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Switch Views")
        self.resize(600, 500)

        # Buttons to switch views
        self.list_btn = QPushButton("List View")
        # ADD: following the format above, create a "Table View"
        self.table_btn = QPushButton("Table View")

        # List and Table Widgets
        self.list_view = QListWidget()
        # ADD: following the format above, add a QTableWidget()
        self.table_view = QTableWidget()

        # Load data once
        self.data = self.load_csv()

        # Layout
        layout = QVBoxLayout(self)
        layout.addWidget(self.list_btn)
        layout.addWidget(self.list_view)

        # ADD: add new widgets (button and table) here
        layout.addWidget(self.table_view)
        layout.addWidget(self.table_btn)

        # Fill both views initially
        for row in self.data[1:]:
            self.list_view.addItem(f"Producer: {row[0]} Model: {row[1]}")
            # ADD: insert the name of your table in the space
            self.fill_table(self.data)

        # Show only the list view first
        self.list_view.show()
        self.table_view.hide()

        # Connect buttons
        self.list_btn.clicked.connect(self.show_list)
        self.table_btn.clicked.connect(self.show_table)

    # ADD: following the format above, add a connection to the button to table

    def load_csv(self):
        with open("sample_cars.csv", newline='', encoding='utf-8') as f:
            return list(csv.reader(f))

    # Fill the table with CSV data
    def fill_table(self, data):
        # replace spaces with name of table
        self.table_view.setColumnCount(len(data[0]))
        self.table_view.setRowCount(len(data) - 1)
        self.table_view.setHorizontalHeaderLabels(data[0])
        for r, row in enumerate(data[1:]):
            for c, cell in enumerate(row):
                self.table_view.setItem(r, c, QTableWidgetItem(cell))

    def show_list(self):
        # Show list, hide table
        self.table_view.hide()
        self.list_view.show()

    def show_table(self):
        # Show table, hide list
        self.list_view.hide()
        self.table_view.show()




if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = ViewSwitcher()
    window.show()
    sys.exit(app.exec())
