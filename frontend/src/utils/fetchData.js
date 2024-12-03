export const fetchData = async (url, options = {}) => {
  try {
    const response = await fetch(url, {
      method: options.method || "GET", 
      headers: {
        "Content-Type": "application/json",
        ...options.headers, 
      },
      body: options.body ? JSON.stringify(options.body) : null, 
      ...options,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Request failed", error);
    throw error;
  }
};

export const getReq = (url, headers = {}) =>
  fetchData(url, { method: "GET", headers });

export const postReq = (url, body, headers = {}) =>
  fetchData(url, { method: "POST", body, headers });

export const putReq = (url, body, headers = {}) =>
  fetchData(url, { method: "PUT", body, headers });

export const delReq = (url, headers = {}) =>
  fetchData(url, { method: "DELETE", headers });