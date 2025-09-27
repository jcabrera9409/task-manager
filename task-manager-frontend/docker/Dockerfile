FROM nginx:1.27-alpine

RUN apk add --no-cache dumb-init

RUN addgroup -S webgroup && \
    adduser -S webuser -G webgroup

COPY --chown=webuser:webgroup dist/task-manager-frontend/browser/ /usr/share/nginx/html/

RUN chown -R webuser:webgroup /var/cache/nginx && \
    chown -R webuser:webgroup /var/log/nginx && \
    chown -R webuser:webgroup /etc/nginx/conf.d && \
    touch /var/run/nginx.pid && \
    chown webuser:webgroup /var/run/nginx.pid

USER webuser

EXPOSE 80

ENTRYPOINT ["dumb-init", "--"]
CMD ["nginx", "-g", "daemon off;"]