/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.utilities;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.handlers.ErrorHandler;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaRESTServices;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a facade around the OMRS API. It transforms the OMRS Exceptions into OMAS exceptions
 */
public class OMRSAPIHelper {

    // logging
    private static final Logger log = LoggerFactory.getLogger(OMRSAPIHelper.class);
    private static final String className = OMRSAPIHelper.class.getName();

    private OMRSMetadataCollection oMRSMetadataCollection=null;
    private ErrorHandler errorHandler=null;
    private String serviceName="Subject Area OMAS";
    private String serverName = null;
    private OMRSRepositoryConnector omrsConnector = null;

    public OMRSMetadataCollection getOMRSMetadataCollection() throws PropertyServerException {
        validateInitialization();
        return oMRSMetadataCollection;
    }
    public OMRSRepositoryHelper getOMRSRepositoryHelper() {
        return omrsConnector.getRepositoryHelper();
    }
    public void setOMRSRepositoryConnector(OMRSRepositoryConnector connector) throws PropertyServerException {
        //TODO pass the API name down the call stack
        String restAPIName ="";
        String methodName = "setOMRSRepositoryConnector";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + "connector="+connector);
        }

        if (connector==null ) {
            // TODO error
        }

        try {
            this.oMRSMetadataCollection = connector.getMetadataCollection();
        } catch ( org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.PROPERTY_SERVER_ERROR;
            String                 errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(e.getMessage(),
                    restAPIName,
                    serviceName,
                    serverName);
            log.error(errorMessage);
            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    restAPIName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
    }

    /**
     * Validate that this access service has been initialized before attempting to process a request.
     *
     * @throws PropertyServerException - not initialized
     */
    private void validateInitialization() throws PropertyServerException
    {
        String restAPIName= "";
        if (oMRSMetadataCollection == null) {
            this.omrsConnector = SubjectAreaRESTServices.getRepositoryConnector();
            if (this.omrsConnector == null) {
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.SERVICE_NOT_INITIALIZED;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(restAPIName);

                throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        restAPIName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {
                this.setOMRSRepositoryConnector(this.omrsConnector);
            }
        }
    }

    public OMRSAPIHelper() {
    }
    // types
    public TypeDefGallery callGetAllTypes(String userId) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException

    {
        String methodName = "callGetAllTypes";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        TypeDefGallery typeDefGallery=null;

        try {
            typeDefGallery= getOMRSMetadataCollection().getAllTypes(userId);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return typeDefGallery;
    }

    public TypeDef callGetTypeDefByName(String userId, String typeName) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException {
        String methodName = "callGetTypeDefByName";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        TypeDef typeDef =null;
        //TODO cascade
        String restAPIName= "";

        try {
            typeDef= getOMRSMetadataCollection().getTypeDefByName(userId,typeName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException e) {
            this.errorHandler.handleTypeDefNotKnownException(typeName,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return typeDef;
    }

    // entity CRUD
    public EntityDetail callOMRSAddEntity(String userId, EntityDetail entityDetail) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, ClassificationException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.StatusNotSupportedException {
        String methodName = "callOMRSAddEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail  addedEntityDetail=null;


        InstanceProperties instanceProperties = entityDetail.getProperties();
        try {
            addedEntityDetail=getOMRSMetadataCollection().addEntity(userId, entityDetail.getType().getTypeDefGUID(), instanceProperties, entityDetail.getClassifications(), InstanceStatus.ACTIVE);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
            this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
            this.errorHandler.handleStatusNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return addedEntityDetail;
    }
    public EntityDetail callOMRSGetEntityByGuid(String userId, String entityGUID) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,  UnrecognizedGUIDException {

        String methodName = "callOMRSGetEntityByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail  gotEntityDetail=null;

        try {
            gotEntityDetail=  getOMRSMetadataCollection().getEntityDetail(userId, entityGUID);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException e) {
            this.errorHandler.handleEntityProxyOnlyException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return gotEntityDetail;


    }

    public List<EntityDetail> callFindEntitiesByProperty(String             userId,
                                                         String                    entityTypeGUID,
                                                         InstanceProperties        matchProperties,
                                                         MatchCriteria matchCriteria,
                                                         int                       fromEntityElement,
                                                         List<InstanceStatus>      limitResultsByStatus,
                                                         List<String>              limitResultsByClassification,
                                                         Date                      asOfTime,
                                                         String                    sequencingProperty,
                                                         SequencingOrder sequencingOrder,
                                                         int                       pageSize) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException {

        String methodName = "callFindEntitiesByProperty";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        List<EntityDetail>  foundEntities = null;


        try {
            foundEntities =  getOMRSMetadataCollection().findEntitiesByProperty(userId,
                    entityTypeGUID,
                    matchProperties,
                    matchCriteria,
                    fromEntityElement,
                    limitResultsByStatus,
                    limitResultsByClassification,
                    asOfTime,
                    sequencingProperty,
                    sequencingOrder,pageSize);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return foundEntities;
    }

    public EntityDetail callOMRSUpdateEntity(String userId, EntityDetail entityDetail) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,  UnrecognizedGUIDException {
        String methodName = "callOMRSUpdateEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail updatedEntity = null;

        InstanceProperties instanceProperties = entityDetail.getProperties();
        try {
            updatedEntity = getOMRSMetadataCollection().updateEntityProperties(userId, entityDetail.getGUID(), instanceProperties);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(entityDetail.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return updatedEntity ;
    }
    public  EntityDetail callOMRSDeleteEntity(String userId, String typeDefName, String typeDefGuid, String obsoleteGuid) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, UnrecognizedGUIDException {
        String methodName = "callOMRSDeleteEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        EntityDetail deletedEntity =null;

        try {
            deletedEntity   = getOMRSMetadataCollection().deleteEntity(userId,typeDefGuid, typeDefName, obsoleteGuid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return deletedEntity;
    }
    public void callOMRSPurgeEntity(String userId, String typeDefName, String typeDefGuid, String obsoleteGuid) throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.EntityNotDeletedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,  UnrecognizedGUIDException, GUIDNotPurgedException {
        String methodName = "callOMRSPurgeEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        try {
            getOMRSMetadataCollection().purgeEntity(userId, typeDefGuid, typeDefName,  obsoleteGuid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException e) {
            this.errorHandler.handleEntityNotDeletedException(obsoleteGuid,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }

    // entity classification
    public EntityDetail callOMRSClassifyEntity(String userId,
                                               String entityGUID,
                                               String classificationName,
                                               InstanceProperties instanceProperties
    ) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, ClassificationException,  UnrecognizedGUIDException {
        String methodName = "callOMRSClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail entity = null;
        try {
            entity = getOMRSMetadataCollection().classifyEntity(userId, entityGUID, classificationName, instanceProperties);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
            this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return entity;
    }

    public EntityDetail callOMRSDeClassifyEntity(String userId,
                                                 String entityGUID,
                                                 String classificationName
    ) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, ClassificationException,  UnrecognizedGUIDException {

        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";

        EntityDetail entity = null;
        try {
            entity = getOMRSMetadataCollection().declassifyEntity(userId, entityGUID, classificationName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException e) {
            this.errorHandler.handleClassificationErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return entity;
    }


    // relationship CRUD
    public Relationship callOMRSAddRelationship(String userId, Relationship relationship)
            throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException,  UnrecognizedGUIDException {
        String methodName = "callOMRSDeClassifyEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        Relationship addedRelationship =null;
        try {
            addedRelationship =getOMRSMetadataCollection().addRelationship(userId,
                    relationship.getType().getTypeDefGUID(),
                    relationship.getProperties(),
                    relationship.getEntityOneProxy().getGUID(),
                    relationship.getEntityTwoProxy().getGUID(),
                    InstanceStatus.ACTIVE);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
            e.printStackTrace();
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(relationship.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);

        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return addedRelationship;
    }

    public Relationship callOMRSGetRelationshipByGuid(String userId, String relationshipGUID)
            throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, UnrecognizedGUIDException {
        String methodName = "callOMRSGetRelationshipByGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }
        //TODO cascade
        String restAPIName= "";
        Relationship relationship =null;
        try {
            relationship = getOMRSMetadataCollection().getRelationship(userId,relationshipGUID);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(relationshipGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return relationship;
    }
    public Relationship callOMRSUpdateRelationship(String userId, Relationship relationship)
            throws org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.StatusNotSupportedException, UnrecognizedGUIDException {
        String methodName = "callOMRSUpdateRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = "";
        Relationship updatedRelationship = null;
        // update the relationship properties
        try {
            updatedRelationship = getOMRSMetadataCollection().updateRelationshipProperties(userId,
                    relationship.getGUID(),
                    relationship.getProperties());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(relationship.getGUID(),
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        // update the status if required.
        if (!updatedRelationship.getStatus().equals(relationship.getStatus())) {
            try {

                updatedRelationship = getOMRSMetadataCollection().updateRelationshipStatus(userId,
                        relationship.getGUID(),
                        relationship.getStatus());
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
                this.errorHandler.handleInvalidParameterException(e,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
                this.errorHandler.handleRepositoryError(e,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

                this.errorHandler.handleUnauthorizedUser(userId,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException e) {
                this.errorHandler.handleStatusNotSupportedException(e,
                        restAPIName,
                        serverName,
                        serviceName);
            } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
                this.errorHandler.handleRelationshipNotKnownException(relationship.getGUID(),
                        restAPIName,
                        serverName,
                        serviceName);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return updatedRelationship;

    }
    public void callOMRSDeleteRelationship(String userId, String typeGuid, String typeName,String guid)
            throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException, UnrecognizedGUIDException {
        // delete the relationship
        String methodName = "callOMRSDeleteRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = "";
        Relationship updatedRelationship = null;
        try {
            getOMRSMetadataCollection().deleteRelationship(userId, typeGuid, typeName, guid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }
    public void callOMRSPurgeRelationship(String userId, String typeGuid, String typeName,String guid)
            throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, GUIDNotPurgedException, UnrecognizedGUIDException {

        // delete the relationship
        String methodName = "callOMRSPurgeRelationship";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = "";
        Relationship updatedRelationship = null;
        try {
            getOMRSMetadataCollection().purgeRelationship(userId, typeGuid, typeName, guid);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException e) {
            this.errorHandler.handleRelationshipNotKnownException(guid,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotDeletedException e) {
            this.errorHandler.handleRelationshipNotDeletedException(e,
                    restAPIName,
                    serverName,
                    serviceName,guid);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }
    public List<Relationship> callGetRelationshipsForEntity(String                     userId,
                                                            String                     entityGUID)
            throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException, UnrecognizedGUIDException {

        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = "";
        Relationship updatedRelationship = null;
        List<InstanceStatus> statusList = new ArrayList<>();
        statusList.add(InstanceStatus.ACTIVE);
        List<Relationship> relationships = null;
        try {
            relationships = getOMRSMetadataCollection().getRelationshipsForEntity(userId,
                    entityGUID,
                    null,
                    0,
                    statusList,
                    null,
                    null,
                    null,
                    0
            );
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return relationships;
    }

    public List<Relationship> callGetRelationshipsForEntity(String           userId,
                                                            String                     entityGUID,
                                                            String                     relationshipTypeGUID,
                                                            int                        fromRelationshipElement,
                                                            List<InstanceStatus>       limitResultsByStatus,
                                                            Date                       asOfTime,
                                                            String                     sequencingProperty,
                                                            SequencingOrder            sequencingOrder,
                                                            int                        pageSize) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.FunctionNotSupportedException, UnrecognizedGUIDException {
        String methodName = "callGetRelationshipsForEntity";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = "";
        List<Relationship> relationships= null;
        try {
            relationships =  getOMRSMetadataCollection().getRelationshipsForEntity(userId,
                    entityGUID,relationshipTypeGUID,fromRelationshipElement,limitResultsByStatus,asOfTime,
                    sequencingProperty,sequencingOrder,pageSize);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {

            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException e) {
            this.errorHandler.handleTypeErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException e) {
            this.errorHandler.handlePropertyErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException e) {
            this.errorHandler.handleEntitytNotKnownError(entityGUID,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException e) {
            this.errorHandler.handleFunctionNotSupportedException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException e) {
            this.errorHandler.handlePagingErrorException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
        return relationships;
    }

    // type
    public String callGetTypeGuid(String userId, String typeName) throws PropertyServerException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.UserNotAuthorizedException, org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException {
        String methodName = "callgetTypeGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        //TODO cascade
        String restAPIName = "";
        TypeDef typeDef = null;
        try {
            typeDef = getOMRSMetadataCollection().getTypeDefByName(userId, typeName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e) {
            this.errorHandler.handleInvalidParameterException(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (RepositoryErrorException e) {
            this.errorHandler.handleRepositoryError(e,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            this.errorHandler.handleUnauthorizedUser(userId,
                    restAPIName,
                    serverName,
                    serviceName);
        } catch (TypeDefNotKnownException e) {
            this.errorHandler.handleTypeDefNotKnownException(typeName,
                    restAPIName,
                    serverName,
                    serviceName);
        }

        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return typeDef.getGUID().toString();
    }
}
