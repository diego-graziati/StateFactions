SELECT PersonId
FROM sf_citizenship
WHERE StateId=?
      AND IsClaimResponsible=true;