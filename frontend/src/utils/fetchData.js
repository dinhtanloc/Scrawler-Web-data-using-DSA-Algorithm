export const fetchData = async (url, options = {}) => {
    try {
      const response = await fetch(url, {
        method: 'GET', 
        headers: {
          'Content-Type': 'application/json',
          // 'Authorization': 'Bearer YOUR_TOKEN',
          ...options.headers,
        },
        ...options, 
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
  
      return data; 
    } catch (error) {
      console.error('Request failed', error);
      throw error; 
    }
  };
  