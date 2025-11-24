import sys
from PyQt5.QtCore import QDate
# Adjust the modules and classes based on your needs. Some of these are suggestion.
# Don't include any modules or classes not in use in the final output.
from PyQt5.QtWidgets import QLabel, QComboBox, QCalendarWidget, QDialog, QApplication, QGridLayout, QSpinBox, \
    QVBoxLayout
from datetime import datetime
import csv


class StockTradeProfitCalculator(QDialog):
    """
    Provide the following functionality:

    - Allows the selection of the stock to be purchased
    - Allows the selection of the quantity to be purchased
    - Allows the selection of the purchase date
    - Displays the purchase total
    - Allows the selection of the sell date
    - Displays the sell total
    - Displays the profit total

    Delete all template comments in the final output, and include your own.
    """

    def __init__(self):
        """
        This method requires substantial updates.
        Each of the widgets should be suitably initialized and laid out.
        """
        super().__init__()

        # Initializing layout of calculator
        layout = QVBoxLayout()
        self.setLayout(layout)

        # setting up dictionary of Stocks
        self.data = self.make_data()

        # Check if 'Amazon' exists, if not, handle it gracefully
        if 'Amazon' in self.data:
            self.sellCalendarDefaultDate = sorted(self.data['Amazon'].keys())[-1]
        else:
            print("Amazon not found in the dataset. Available stocks:", self.data.keys())
            self.sellCalendarDefaultDate = QDate.currentDate()  # Default to the current date

        # Define buyCalendarDefaultDate (two weeks before most recent sell date)

        if 'Amazon' in self.data and self.data['Amazon']:
            last_date_tuple = sorted(self.data['Amazon'].keys())[-1]  # (year, month, day)
            sell_date = QDate(last_date_tuple[0], last_date_tuple[1], last_date_tuple[2])
            self.sellCalendarDefaultDate = sell_date

            # Calculate two weeks before sell date
            self.buyCalendarDefaultDate = sell_date.addDays(-14)
        else:
            print("Amazon not found or has no data. Defaulting to current date.")
            self.sellCalendarDefaultDate = QDate.currentDate()
            self.buyCalendarDefaultDate = QDate.currentDate().addDays(-14)

        # Create QLabel for Stock selection
        label_select_stock = QLabel('Select Stock:', self)
        label_quantity = QLabel('Quantity:', self)

        # Create QComboBox and populate it with a list of Stocks
        self.stock_list = QComboBox(self)
        list_of_stocks = ['Natural_Gas', 'Crude_oil', 'Copper', 'Bitcoin', 'Platinum', 'Ethereum', 'S&P_500',
                          'Nasdaq_100', 'Apple', 'Tesla', 'Microsoft', 'Silver', 'Google', 'Nvidia', 'Berkshire',
                          'Netflix', 'Amazon', 'Meta', 'Gold']

        # Sorting stock in alphabetical order
        list_of_stocks.sort()

        # Adding each of the stock entity in array to box option
        for stock in list_of_stocks:
            self.stock_list.addItem(stock)

        # Create CalendarWidgets for selection of purchase and sell dates
        purchase_date_label = QLabel('Select Purchase Date:', self)
        self.purchase_calendar = QCalendarWidget(self)
        # remove week number from widget
        self.purchase_calendar.setVerticalHeaderFormat(QCalendarWidget.NoVerticalHeader)

        sell_date_label = QLabel('Select Sell Date:', self)
        self.sell_calendar = QCalendarWidget(self)
        # remove week number from widget
        self.sell_calendar.setVerticalHeaderFormat(QCalendarWidget.NoVerticalHeader)

        # Create QSpinBox to select Stock quantity purchased
        self.quantity = QSpinBox(self)

        # Create QLabels to show the Stock purchase total
        self.purchase_total = QLabel("Purchase Total:", self)
        self.purchase_total.setObjectName('purchase_total_label')

        # Create QLabels to show the Stock sell total
        self.sell_total = QLabel("Sell Total:", self)
        self.sell_total.setObjectName('sell_total_label')

        # Create QLabels to show the Stock profit total
        self.stock_profit_total = QLabel("Stock Profit Total:", self)
        self.stock_profit_total.setObjectName('stock_profit_total_label')

        # Initialize the layout - 6 rows to start
        grid = QGridLayout()

        # 6 row version
        """
        grid.addWidget(label_select_stock, 0, 0)
        grid.addWidget(self.stock_list, 0, 1)

        grid.addWidget(label_quantity, 1, 0)
        grid.addWidget(quantity, 1, 1)
        grid.addWidget(purchase_date_label, 2, 0)
        grid.addWidget(purchase_calendar, 2, 1)
        grid.addWidget(sell_date_label, 3, 0)
        grid.addWidget(sell_calendar, 3, 1)
        grid.addWidget(purchase_total, 4, 0)
        grid.addWidget(sell_total, 5, 0)
        grid.addWidget(stock_profit_total, 6, 0)
        """
        grid.addWidget(label_select_stock, 0, 0)
        grid.addWidget(self.stock_list, 1, 0)

        # 10 row version

        grid.addWidget(label_quantity, 2, 0)
        grid.addWidget(self.quantity, 3, 0)
        grid.addWidget(purchase_date_label, 4, 0)
        grid.addWidget(self.purchase_calendar, 5, 0)
        grid.addWidget(sell_date_label, 6, 0)
        grid.addWidget(self.sell_calendar, 7, 0)
        grid.addWidget(self.purchase_total, 8, 0)
        grid.addWidget(self.sell_total, 9, 0)
        grid.addWidget(self.stock_profit_total, 11, 0)

        layout.addLayout(grid)

        # Set default calendar values
        self.purchase_calendar.setSelectedDate(self.buyCalendarDefaultDate)
        self.sell_calendar.setSelectedDate(self.sellCalendarDefaultDate)

        # Also restrict the sell calendar to not allow future dates
        self.sell_calendar.setMaximumDate(QDate.currentDate())

        # Optionally, restrict the purchase calendar so it can't be after the sell date
        self.purchase_calendar.setMaximumDate(self.sellCalendarDefaultDate)

        # Connecting signals to slots so that a change in one control updates the UI
        self.stock_list.currentIndexChanged.connect(self.updateUi)
        self.quantity.valueChanged.connect(self.updateUi)
        self.purchase_calendar.selectionChanged.connect(self.updateUi)
        self.sell_calendar.selectionChanged.connect(self.updateUi)

        # Set the window title
        self.setWindowTitle('Stock Trade Profit Calculator')

        # TODO: update the UI

    def updateUi(self):
        """
        This requires substantial development.
        Updates the UI when control values are changed; should also be called when the app initializes.
        """
        try:
            # Get selected dates
            purchase_year, purchase_month, purchase_day = self.purchase_calendar.selectedDate().getDate()
            sell_year, sell_month, sell_day = self.sell_calendar.selectedDate().getDate()

            # Verify valid date order
            purchase_date = QDate(purchase_year, purchase_month, purchase_day)
            sell_date = QDate(sell_year, sell_month, sell_day)

            if sell_date < purchase_date:
                self.purchase_total.setText("Purchase Total: Invalid date selection")
                self.sell_total.setText("Sell Total: Invalid date selection")
                self.stock_profit_total.setText("Profit: N/A (Sell date before Purchase date)")
                return

            # Get stock name and quantity
            selected_stock = self.stock_list.currentText()
            selected_quantity = self.quantity.value()

            # Verification: Ensure valid quantity
            if selected_quantity <= 0:
                self.purchase_total.setText("Purchase Total: Quantity must be > 0")
                self.sell_total.setText("Sell Total: Quantity must be > 0")
                self.stock_profit_total.setText("Profit: N/A")
                return

            # Ensure stock exists in dataset
            if selected_stock not in self.data:
                self.purchase_total.setText(f"Purchase Total: {selected_stock} not in dataset")
                self.sell_total.setText("Sell Total: N/A")
                self.stock_profit_total.setText("Profit: N/A")
                return

            purchase_tuple = (purchase_year, purchase_month, purchase_day)
            sell_tuple = (sell_year, sell_month, sell_day)

            purchase_rate = self.data[selected_stock].get(purchase_tuple)
            sell_rate = self.data[selected_stock].get(sell_tuple)

            # Verify price data exists
            if purchase_rate is None:
                self.purchase_total.setText("Purchase Total: No data for purchase date")
                self.sell_total.setText("Sell Total: N/A")
                self.stock_profit_total.setText("Profit: N/A")
                return

            if sell_rate is None:
                self.purchase_total.setText(f"Purchase Total: ${selected_quantity * purchase_rate:,.2f}")
                self.sell_total.setText("Sell Total: No data for sell date")
                self.stock_profit_total.setText("Profit: N/A")
                return

            # Perform calculations
            purchase_amount = selected_quantity * purchase_rate
            sell_amount = selected_quantity * sell_rate
            profit_amount = sell_amount - purchase_amount

            # Handle zero or unrealistic values
            if purchase_rate == 0.0 or sell_rate == 0.0:
                self.purchase_total.setText("Purchase Total: Missing or zero price data")
                self.sell_total.setText("Sell Total: Missing or zero price data")
                self.stock_profit_total.setText("Profit: N/A")
                return

            # Update labels
            self.purchase_total.setText(f"Purchase Total: ${purchase_amount:,.2f}")
            self.sell_total.setText(f"Sell Total: ${sell_amount:,.2f}")
            self.stock_profit_total.setText(f"Profit: ${profit_amount:,.2f}")

            # Debug statements
            print()
            print("---------------")
            print(f"Purchase date {purchase_date}")
            print(f"Sell date {sell_date}")
            print()
            print(f"Purchase Rate: {purchase_rate}")
            print(f"Sell Rate: {sell_rate}")
            print("---------------")


        except Exception as e:
            print(f"Error in updateUi: {e}")
            self.purchase_total.setText("Purchase Total: Error")
            self.sell_total.setText("Sell Total: Error")
            self.stock_profit_total.setText("Profit: Calculation failed")

    def make_data(self):
        """
        This code reads the stock market CSV file and generates a dictionary structure.
        :return: a dictionary of dictionaries
        """

        data = {}
        try:
            with open('Stock_Market_Dataset.csv', mode='r') as file:
                reader = csv.DictReader(file)
                stock_names = reader.fieldnames[1:]  # All columns except 'Date' are stock names

                for row in reader:
                    date_string = row['Date']
                    date_tuple = self.string_date_into_tuple(date_string)

                    for stock in stock_names:
                        price = row[stock].replace(',', '')
                        try:
                            price = float(price)
                        except ValueError:
                            price = 0.0

                        if stock not in data:
                            data[stock] = {}

                        data[stock][date_tuple] = price

            print("Data loaded successfully.")
            print(f"Stocks available: {stock_names}")  # Debugging: Print all available stock names


        except Exception as e:
            print(f"Error reading data: {e}")
        return data

    def string_date_into_tuple(self, date_string):
        """
        # Converts a date in string format (e.g., "2024-02-02") into a tuple (year, month, day).
        # :return: tuple representing the date
        """
        try:
            if '-' in date_string:
                date_obj = datetime.strptime(date_string, "%d-%m-%Y")
            else:
                date_obj = datetime.strptime(date_string, "%m/%d/%Y")
            return date_obj.year, date_obj.month, date_obj.day
        except ValueError:
            print(f"Error parsing date: {date_string}")
            return None


# This is complete
if __name__ == '__main__':
    app = QApplication(sys.argv)

    # Connect the external stylesheet
    with open("style.qss", "r") as f:
        app.setStyleSheet(f.read())

    stock_calculator = StockTradeProfitCalculator()
    stock_calculator.show()
    sys.exit(app.exec())
