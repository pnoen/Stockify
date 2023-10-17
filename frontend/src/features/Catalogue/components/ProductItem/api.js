import { getDownloadURL, ref } from "firebase/storage";
import storage from "../../../../firebase";

const getImageUrl = async (imageName) => {
  const imageRef = ref(storage, imageName);
  return getDownloadURL(imageRef);
};

export {getImageUrl}
