# Shopping Cart

## Product Description

This project is a Java-based Shopping Cart application.

It is designed to simulate a store interface where users can interact with a shopping cart in a
command-line environment.

### Features

* **Add Items to Cart:** Users can add items to their shopping cart by providing item names, details
  and price.
* **Remove Items from Cart:** Users can remove items from their cart if they no longer want them.
* **View Cart:** Users can view all items currently in their shopping cart along with the price of
  each item and the total cost.
* **Export Cart:** The program generates a JSON file containing the details of all the items in the
  shopping cart. The file is exported to the `c:\temp` directory by default.
* **Validation:** Prevents adding duplicate items to the cart.

## Technical Steps

Below are the detailed steps for setting up, running, building, testing, and using this project.

### System Requirements

To run and build this project locally, ensure the following are installed on your system:

* **Java Development Kit (JDK)**: Version 21 or
  later. [installation-guide](https://docs.oracle.com/en/java/javase/21/install/overview-jdk-installation.html)
* **Maven**: For build automation. [installation-guide](https://maven.apache.org/install.html)

Case need to use version 8, you can run with the [profile](#profile).

### Building the Project

To build the project locally, follow these steps:

* Clone the repository:
   ```bash
   git clone https://github.com/AnyST/shopping-cart
   cd shopping-cart
   ```
* Then, build the project by running:
   ```bash
   ./mvnw clean install
   ```

If you want to build in a compatible way with JDK8 you may use the maven <a id="profile"></a>
profile by running

   ```bash
   ./mvnw clean install -P jdk-8
   ```

These will create the compiled JAR file in the `target` directory.

### Unit Testing and Coverage

To test the application using the command line:

* Execute the tests using this maven command:
   ```bash
   ./mvnw test
   ```
* Check the coverage of the unit tests to ensure 80% or more of the code paths are tested.
  The reports will be found under the `target/site/jacoco` directory.
  The report can be accessed in the file `index.html`.

### Executing the Program Locally

To run the program locally using the command line:

* Navigate to your project directory if necessary.
   ```bash
   cd shopping-cart
   ```
* Run the application by one of them:
   ```bash
  ./mvnw package exec:java -Dexec.mainClass=com.natixis.shoppingcart.App -DskipTests
  ```
  ```bash
   java -jar target/shopping-cart-0.1.0-SNAPSHOT.jar
   ```
* Follow the on-screen menu options to interact with the program or read the user manual below.

---

## Usage Instructions

### Start the Program:

- Open a terminal or command prompt.
- Navigate to the directory containing the JAR file.
- Start the program using:
  ```bash
  java -jar shopping-cart-0.1.0-SNAPSHOT.jar
  ```

### Menu Navigation:

- Upon starting, you will see a menu:
  ```text
  Welcome to the store

  Choose an action:
  1. Add item to cart
  2. Remove item from cart
  3. View cart
  4. Export cart
  5. Exit

  Type a number from 1 to 5 to choose an action.
  ```

### Available Actions:

- **Add Item**:
  - You must input the product name.
    ```text
    Enter item name:
    ```
  - Then you must input the product description.
    ```text
    Enter item description:
    ```
  - Then you must input the product price.
    ```text
    Enter item price:
    ```
  - Then the message will be displayed:
    ```text
    Added item:<item-name>
    ```
- **Remove Item**:
  - Enter the name of the item you want to remove.
    ```text
    Enter item name to remove:
    ```
- **View Cart**:
  - Displays the full cart, including item names, prices, and total cost.
    ```text
    This is your cart:
    ```
    ```json
    {
      "sum": 1999.9,
      "items": {
        "notebook": 1999.9
      }
    }
    ```
- **Export Cart**:
  - Saves the cart into a JSON file located at `c:\temp`.
  - Example output:
    ```text
    Cart exported to file at: c:\temp\cart.json
    ```
- **Exit**:
  - The message will be displayed:
    ```text
    Thank you for shopping. Goodbye!
    ```
  - Then the program will shut off.
