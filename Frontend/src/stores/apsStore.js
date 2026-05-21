class apsStore {
  constructor() {
    this.apsList = [];
  }

  setApsList(apsList) {
    this.apsList = apsList;
  }

  get apsCount() {
    return this.apsList.length;
  }

}

export default new apsStore();
