<!-- HEADER -->
<br/>
<div align="center">
  <img src="frontend/src/assets/logoGreen.png" alt="Logo" width="80" height="80">

  <h3 align="center">Stockify</h3>

  <p align="center">Inventory Management System</p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a>
          <ul>
            <li><a href="#backend">Backend</a></li>
            <li><a href="#frontend">Frontend</a></li>
          </ul>
        </li>
      </ul>
    </li>
    <li>
      <a href="#working-functionalities">Working Functionalities</a>
      <ul>
        <li><a href="#services-and-functionalities">Services and Functionalities</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#running-the-service">Running the Service</a></li>
      </ul>
    </li>
  </ol>
</details>


<!-- ABOUT THE PROJECT -->
## About The Project

Stockify is our customised solution to the problems observed in current stock inventory management systems. We have found a particular use case of it applied in the construction industry, where there is excess stock that is normally not tracked. A business may have a batch order for new steel beams, and the supplier will send them their steel beams. The business will cut the amount needed for their project and then rest be stored without systematic tracking. This gap in inventory management can lead to inefficiencies when assessing whether there's a need for new orders or if the reserve stock suffices. Stockify hopes to help the business in managing their excess of products and streamline the entire stock management process for both supplier and customer through centralising the process on our online platform.


### Built With

Major frameworks/libraries used in Stockify along with their version:

#### Backend
* [![Spring][Spring.js]][Spring-url] ![Spring Version][Spring-Version]
* [![Java][Java.js]][Java-url] ![Java Version][Java-Version]
* [![MySQL][MySQL.js]][MySQL-url] ![MySQL Version][MySQL-Version]
* [![SendGrid][SendGrid.js]][SendGrid-url] ![SendGrid Version][SendGrid-Version]

#### Frontend
* [![React][React.js]][React-url] ![React Version][React-Version]
* [![MaterialUI][MaterialUI.js]][MaterialUI-url] ![MaterialUI Version][MaterialUI-Version]
* [![Firebase][Firebase.js]][Firebase-url] ![Firebase Version][Firebase-Version]


<!-- Working Functionalities -->
## Working Functionalities

### Services and Functionalities
1. Register and Login - Users can register as a Supplier or a normal Customer. Their account will be associated with their role, and when they log in, will be redirected to either the Supplier or Customer dashboard.
2. Edit and Update User Details - Suppliers can manage their employees on Stockify by adding, deleting, editing employees under their business.
3. Inventory Browsing - Customers can be linked to a Supplier and view their current inventory and available stocks.
4. Update Their Inventory - Suppliers can update their inventory by changing their stocks, adding new products, removing current products and edit existing inventory details.
5. Shopping Basket - Customers add a product and amount they would like to purchase into a shopping basket. The purchase will not be made until they check out from their shopping basket.
6. Order Payment Confirmation and Invoice - Once a customer checks out from their basket, an completed payment invoice is sent both to the Supplier and the Customer via email.
7. View Order History - Customers can view their previous order history.
8. View Statistics - Suppliers can view their open orders and completed orders, as well as their total revenue.


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

Please install the following libraries in order to get started:

1. Node.js and NPM
 
   ```
   MUST be version 14+, instructions can be found on node's website specific to your system
   ```
2. Java Version 17

   ```
   MUST be version 17, refer to java documents for your specifc machine
   ```
3. Gradle

   ```
   MUST be for your system
   ```
   
### Running the Backend Microservices

Please ensure that Node and Java installed on your system. Run backend before running frontend.

1. Clone the repo
   ```sh
   git clone https://github.sydney.edu.au/mkar6206/ELEC5619-Stockify.git
   ```
2. Depending on whether you're on MacOS or Windows, please run the following shell command to run all microservices in the backend folder:
   ```sh
   # Windows
   start_microservices.bat # Run this in the backend folder in Command Prompt
   
   # MacOS
   chmod +x start_microservices.sh # Make your script executable in Terminal
   ./start_microservices.sh # Run this in the backend folder
   ```
### Running the Frontend


1. Run the following commands in the frontend folder to start up the GUI
   ```sh
   npm install
   npm run start
   ```

<!-- MARKDOWN LINKS & IMAGES -->
[Spring.js]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-url]: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
[Spring-Version]: https://img.shields.io/badge/-v3.1.3-6DB33F?style=for-the-badge

[Java.js]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://docs.oracle.com/en/java/
[Java-Version]: https://img.shields.io/badge/-v17-ED8B00?style=for-the-badge

[MySQL.js]: https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white
[MySQL-url]: https://dev.mysql.com/doc/
[MySQL-Version]: https://img.shields.io/badge/-v8.1.0-00000F?style=for-the-badge

[SendGrid.js]: https://img.shields.io/badge/SendGrid-294661?style=for-the-badge&logo=sendgrid&logoColor=white
[SendGrid-url]: https://docs.sendgrid.com/v2-api
[SendGrid-Version]: https://img.shields.io/badge/-v2-294661?style=for-the-badge

[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[React-Version]: https://img.shields.io/badge/-v18.2.0-20232A?style=for-the-badge

[MaterialUI.js]: https://img.shields.io/badge/Material--UI-0081CB?style=for-the-badge&logo=material-ui&logoColor=white
[MaterialUI-url]: https://mui.com/
[MaterialUI-Version]: https://img.shields.io/badge/-v5.14.8-0081CB?style=for-the-badge

[Firebase.js]: https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black
[Firebase-url]: https://firebase.google.com/docs
[Firebase-Version]: https://img.shields.io/badge/-v10.5.0-FFCA28?style=for-the-badge
