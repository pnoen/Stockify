import { initializeApp } from 'firebase/app';
import { getStorage } from "firebase/storage";

const firebaseConfig = {
  apiKey: "AIzaSyCHrRoH_mrET7fgBXR7G7d9ytvIV-gOSPA",
  authDomain: "stockify-c696e.firebaseapp.com",
  projectId: "stockify-c696e",
  storageBucket: "stockify-c696e.appspot.com",
  messagingSenderId: "1071884555510",
  appId: "1:1071884555510:web:4c00f11ed2823e8084c8d7",
  measurementId: "G-X1DH7HFW2C"
};

const app = initializeApp(firebaseConfig);
var storage = getStorage(app);
export default storage;