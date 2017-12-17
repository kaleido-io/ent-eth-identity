var Web3 = require('web3');

module.exports = {
networks: {
    development: {
      provider: () => {
        return new Web3.providers.HttpProvider("https://quorum.photic.io/nodes/pfgo97ju-node-9ujnsw9k");
      },
      network_id: "*", // Match any network id
      gasPrice: 0,
      gas: 4500000
    }
  }
};
