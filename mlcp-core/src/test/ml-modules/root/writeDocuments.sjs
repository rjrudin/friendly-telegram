'use strict';

declareUpdate();

function normalizeInputToArray(input) {
  var inputArray;
  if (input instanceof Sequence) {
    inputArray = input.toArray().map(item => fn.head(xdmp.fromJSON(item)));
  } else if (input instanceof Document) {
    inputArray = [fn.head(xdmp.fromJSON(input))];
  } else {
    // Assumed to be an array at this point, which is the case for unit tests
    inputArray = fn.head(xdmp.fromJSON(input));
  }
  return inputArray;
}

var endpointConstants = fn.head(xdmp.fromJSON(endpointConstants));
const inputArray = normalizeInputToArray(input);

inputArray.forEach(record => {
  xdmp.documentInsert("/doc/" + sem.uuidString() + ".json", record,
    [xdmp.permission("rest-reader", "read"), xdmp.permission("rest-writer", "update")],
    "mlcp-data"
  );
});
